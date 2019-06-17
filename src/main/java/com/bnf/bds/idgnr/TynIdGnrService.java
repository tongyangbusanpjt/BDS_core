package com.bnf.bds.idgnr;

import java.math.BigDecimal;

import egovframework.rte.fdl.cmmn.exception.FdlException;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;

/**
 * 
 * 
 */
public interface TynIdGnrService extends EgovIdGnrService {
	
	BigDecimal getNextBigDecimalId(String group) throws FdlException;
	
	long getNextLongId(String group) throws FdlException;
	
	int getNextIntegerId(String group) throws FdlException;
	
	String getNextStringId(String group) throws FdlException;
}
