package com.bnf.bds.idgnr;


import java.math.BigDecimal;
import java.util.Locale;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import egovframework.rte.fdl.cmmn.exception.FdlException;
import egovframework.rte.fdl.idgnr.impl.AbstractDataBlockIdGnrService;

/**
 * 
 * 
 */
public class TynTableIdGnrServiceImpl extends AbstractDataBlockIdGnrService implements TynIdGnrService {
	
	private static final Logger log = LoggerFactory.getLogger(TynTableIdGnrServiceImpl.class);
	
	private String table = "ids";
	
	private String tableName = "id";
	
//	private String groupName = "group";
	
	private String tableNameFieldName = "table_name";
	
	private String groupValFieldName = "group_val";
	
	private String nextIdFieldName = "next_id";
	
	private JdbcTemplate jdbcTemplate;
	
	private TransactionTemplate transactionTemplate;
	
	public TynTableIdGnrServiceImpl() {
		
	}
	
	@Override
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
		
		jdbcTemplate = new JdbcTemplate(dataSource);
		
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
		transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
	}
	
	@Override
	protected BigDecimal allocateBigDecimalIdBlock(int blockSize) throws FdlException {
		return (BigDecimal) allocateIdBlock(blockSize, true);
	}

	@Override
	protected long allocateLongIdBlock(int blockSize) throws FdlException {
		return ((Long) allocateIdBlock(blockSize, false)).longValue();
	}
	
	
	
	private Object allocateIdBlock(final int blockSize, final boolean useBigDecimals) throws FdlException {

//		log.debug(messageSource.getMessage("debug.idgnr.allocate.idblock", new Object[] { new Integer(blockSize), tableName }, Locale.getDefault()));

		try {
			return transactionTemplate.execute(new TransactionCallback<Object>() {
				
				@SuppressWarnings("deprecation")
				public Object doInTransaction(TransactionStatus status) {

					Object nextId;
					Object newNextId;

					try {
						String selectQuery = "SELECT " + nextIdFieldName + " FROM " + table + " WHERE " + tableNameFieldName + " = ?";

						log.debug("Select Query : {}", selectQuery);

						if (useBigDecimals) {
							try {
								nextId = jdbcTemplate.queryForObject(selectQuery, new Object[] { tableName }, BigDecimal.class);
							} catch (EmptyResultDataAccessException erdae) {
								nextId = null;
							}

							if (nextId == null) { // no row
								insertInitId(useBigDecimals, blockSize);

								return new BigDecimal(0);
							}
						} else {
							try {
								nextId = jdbcTemplate.queryForLong(selectQuery, tableName);
							} catch (EmptyResultDataAccessException erdae) {
								nextId = -1L;
							}

							if ((Long) nextId == -1L) { // no row
								insertInitId(useBigDecimals, blockSize);

								return new Long(0);
							}
						}
					} catch (DataAccessException dae) {
						log.debug("{}", dae);
						status.setRollbackOnly();
						throw new RuntimeException(new FdlException(messageSource, "error.idgnr.select.idblock", new String[] { tableName }, null));
					}

					try {
						String updateQuery = "UPDATE " + table + " SET " + nextIdFieldName + " = ?" + " WHERE " + tableNameFieldName + " = ?";

						log.debug("Update Query : {}", updateQuery);

						if (useBigDecimals) {
							newNextId = ((BigDecimal) nextId).add(new BigDecimal(blockSize));

						} else {
							newNextId = new Long(((Long) nextId).longValue() + blockSize);
						}

						jdbcTemplate.update(updateQuery, newNextId, tableName);

						return nextId;
					} catch (DataAccessException dae) {
						status.setRollbackOnly();
						throw new RuntimeException(new FdlException(messageSource, "error.idgnr.update.idblock", new String[] { tableName }, null));
					}
				}
			});
		} catch (RuntimeException re) {
			if (re.getCause() instanceof FdlException) {
				throw (FdlException) re.getCause();
			} else {
				throw re;
			}
		}
	}
	
	private Object allocateIdBlock(final int blockSize, final boolean useBigDecimals, final String groupVal) throws FdlException {
//		log.debug(messageSource.getMessage("debug.idgnr.allocate.idblock", new Object[] { new Integer(blockSize), tableName }, Locale.getDefault()));
		
		try {
			return transactionTemplate.execute(new TransactionCallback<Object>() {
				
				@SuppressWarnings("deprecation")
				public Object doInTransaction(TransactionStatus status) {
					
					Object nextId;
					Object newNextId;
					
					try {
						String selectQuery = "SELECT " + nextIdFieldName + " FROM " + table + " WHERE " + tableNameFieldName + " = ? AND " + groupValFieldName + " = ?";
						
						log.debug("Select Query : {}", selectQuery);
						
						if (useBigDecimals) {
							try {
								nextId = jdbcTemplate.queryForObject(selectQuery, new Object[] { tableName, groupVal }, BigDecimal.class);
							}
							catch (EmptyResultDataAccessException erdae) {
								nextId = null;
							}
							
							if (nextId == null) {
								insertInitId(useBigDecimals, blockSize, groupVal);
								
								return new BigDecimal(0);
							}
						}
						else {
							try {
								nextId = jdbcTemplate.queryForLong(selectQuery, tableName, groupVal);
							}
							catch (EmptyResultDataAccessException erdae) {
								nextId = -1L;
							}
							
							if ((Long) nextId == -1L) {
								insertInitId(useBigDecimals, blockSize, groupVal);
								
								return new Long(0);
							}
						}
					}
					catch (DataAccessException dae) {
						log.error("{}", dae);
						status.setRollbackOnly();
						
						throw new RuntimeException(new FdlException(messageSource, "error.idgnr.select.idblock", new String[] { tableName }, null));
					}
					
					try {
						String updateQuery = "UPDATE " + table + " SET " + nextIdFieldName + " = ?" + " WHERE " + tableNameFieldName + " = ? AND " + groupValFieldName + " = ?";
						
						log.debug("Update Query : {}", updateQuery);
						
						if (useBigDecimals) {
							newNextId = ((BigDecimal) nextId).add(new BigDecimal(blockSize));
						}
						else {
							newNextId = new Long(((Long) nextId).longValue() + blockSize);
						}
						
						jdbcTemplate.update(updateQuery, newNextId, tableName, groupVal);
						
						return nextId;
					}
					catch (DataAccessException dae) {
						status.setRollbackOnly();
						
						throw new RuntimeException(new FdlException(messageSource, "error.idgnr.update.idblock", new String[] { tableName }, null));
					}
				}
			});
		}
		catch (RuntimeException re) {
			if (re.getCause() instanceof FdlException) {
				throw (FdlException) re.getCause();
			}
			else {
				throw re;
			}
		}
	}
	
	private Object insertInitId(final boolean useBigDecimals, final int blockSize) {
//		log.debug(messageSource.getMessage("debug.idgnr.init.idblock", new Object[] { tableName }, Locale.getDefault()));

		Object initId = null;

		String insertQuery = "INSERT INTO " + table + "(" + tableNameFieldName + ", " + nextIdFieldName + ") " + "values('" + tableName + "', ?)";

		log.debug("Insert Query : {}", insertQuery);

		if (useBigDecimals) {
			initId = new BigDecimal(blockSize);

		} else {
			initId = new Long(blockSize);
		}

		jdbcTemplate.update(insertQuery, initId);

		return initId;

	}
	
	private Object insertInitId(final boolean useBigDecimals, final int blockSize, final String groupVal) {
//		log.debug(messageSource.getMessage("debug.idgnr.init.idblock", new Object[] { tableName }, Locale.getDefault()));
		
		Object initId = null;
		
		String insertQuery = "INSERT INTO " + table + "(" + tableNameFieldName + ", " + groupValFieldName + ", " + nextIdFieldName + ") " + "values(?, ?, ?)";
		
		log.debug("Insert Query : {}", insertQuery);
		
		if (useBigDecimals) {
			initId = new BigDecimal(blockSize);
		}
		else {
			initId = new Long(blockSize);
		}
		
		jdbcTemplate.update(insertQuery, new Object[] {tableName, groupVal, initId});
		
		return initId;
	}
	
	
	public void setTable(String table) {
		this.table = table;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public void setGroupName(String groupName) {
	//	this.groupName = groupName;
	}
	
	public void setTableNameFieldName(String tableNameFieldName) {
		this.tableNameFieldName = tableNameFieldName;
	}
	
	public void setGroupValFieldName(String groupValFieldName) {
		this.groupValFieldName = groupValFieldName;
	}
	
	public void setNextIdFieldName(String nextIdFieldName) {
		this.nextIdFieldName = nextIdFieldName;
	}
	
	
	////////////////////////////////////////////////////////////////////////////
	
	private final Object semaphore = new Object();
	
	public BigDecimal getNextBigDecimalId(String group) throws FdlException {
		BigDecimal bd;
		
		if (useBigDecimals) {
			synchronized (semaphore) {
			//	bd = (BigDecimal) allocateIdBlock(blockSize, true, group);
				bd = (BigDecimal) allocateIdBlock(1, true, group);
			}
		}
		else {
			synchronized (semaphore) {
			//	bd = new BigDecimal(((Long) allocateIdBlock(blockSize, false, group)).longValue());
				bd = new BigDecimal(((Long) allocateIdBlock(1, false, group)).longValue());
			}
		}
		
		return bd;
	}
	
	public long getNextLongId(String group) throws FdlException {
		return getNextBigDecimalId(group).longValue();
	}
	
	public int getNextIntegerId(String group) throws FdlException {
		return getNextBigDecimalId(group).intValue();
	}
	
	@Override
	public String getNextStringId(String group) throws FdlException {
		return getStrategy().makeId(getNextBigDecimalId(group).toString());
	}
}
