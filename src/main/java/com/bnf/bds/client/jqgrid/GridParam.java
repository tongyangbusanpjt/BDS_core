package com.bnf.bds.client.jqgrid;

import org.apache.commons.lang3.builder.*;

import com.fasterxml.jackson.annotation.*;
/**
 * jqGrid 그리드 파라메터
 * 서비스의 VO 를 구현할때 이 클래스를 상속 받을 것.
 * 
 * $.jgrid.defaults.prmNames 객체와 동일하게 정의되어 있어야 함.
 * 
 * @author 송정헌
 */
public class GridParam {
	
	
	private String _userId;
	private String pjtId;
	
	public String get_userId() {
		return _userId;
	}
	public void set_userId(String _userId) {
		this._userId = _userId;
	}
	public String getPjtId() {
		return pjtId;
	}
	public void setPjtId(String pjtId) {
		this.pjtId = pjtId;
	}



	private String _status;
	
	/** jqGrid option >>
		prmNames: {
			page:      "page",
			rows:      "rows",
			sort:      "sidx",
			order:     "sord",
			search:    "_search",
			nd:        "nd",
			id:        "id",
			oper:      "oper",
			editoper:  "edit",
			addoper:   "add",
			deloper:   "del",
			subgridid: "id",
			npage:     null,
			totalrows: "totalrows"
		}
	 */
	@JsonProperty(access =JsonProperty.Access.WRITE_ONLY)
	private int		_page;		// requested page
	@JsonProperty(access =JsonProperty.Access.WRITE_ONLY)
	private int		_rows; 		// number of rows requested
	@JsonProperty(access =JsonProperty.Access.WRITE_ONLY)
	private String	_sidx;		// sorting column
	@JsonProperty(access =JsonProperty.Access.WRITE_ONLY)
	private String	_sord;		// sort order
	@JsonProperty(access =JsonProperty.Access.WRITE_ONLY)
	private boolean	_search;	// search indicator
	@JsonProperty(access =JsonProperty.Access.WRITE_ONLY)
	private long	_nd;		// time passed to the request
	private String	_id;		// name of the id when POST-ing data in editing modules
	@JsonProperty(access =JsonProperty.Access.WRITE_ONLY)
	private String	_oper;		// operation parameter
	@JsonProperty(access =JsonProperty.Access.WRITE_ONLY)
	private String	_edit;		// name of operation when the data is posted in edit mode
	@JsonProperty(access =JsonProperty.Access.WRITE_ONLY)
	private String	_add;		// name of operation when the data is posted in add mode
	@JsonProperty(access =JsonProperty.Access.WRITE_ONLY)
	private String	_del;		// name of operation when the data is posted in delete mode
	@JsonProperty(access =JsonProperty.Access.WRITE_ONLY)
	private String	_subgridid;	// name passed when we click to load data in the sub-grid
	@JsonProperty(access =JsonProperty.Access.WRITE_ONLY)
	private int		_npage;		// ?
	@JsonProperty(access =JsonProperty.Access.WRITE_ONLY)
	private int		_totalrows;	// the number of the total rows to be obtained from server
	@JsonProperty(access =JsonProperty.Access.WRITE_ONLY)
	private String	_sidxcol;
	
	public String get_status() {
		return _status;
	}
	public void set_status(String _status) {
		this._status = _status;
	}
	public int get_page() {
		return _page;
	}
	public void set_page(int _page) {
		this._page = _page;
	}
	public int get_rows() {
		return _rows;
	}
	public void set_rows(int _rows) {
		this._rows = _rows;
	}
	public String get_sidx() {
		return _sidx;
	}
	public void set_sidx(String _sidx) {
		this._sidx = _sidx;
	}
	public String get_sord() {
		return _sord;
	}
	public void set_sord(String _sord) {
		this._sord = _sord;
	}
	public boolean is_search() {
		return _search;
	}
	public void set_search(boolean _search) {
		this._search = _search;
	}
	public long get_nd() {
		return _nd;
	}
	public void set_nd(long _nd) {
		this._nd = _nd;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String get_oper() {
		return _oper;
	}
	public void set_oper(String _oper) {
		this._oper = _oper;
	}
	public String get_edit() {
		return _edit;
	}
	public void set_edit(String _edit) {
		this._edit = _edit;
	}
	public String get_add() {
		return _add;
	}
	public void set_add(String _add) {
		this._add = _add;
	}
	public String get_del() {
		return _del;
	}
	public void set_del(String _del) {
		this._del = _del;
	}
	public String get_subgridid() {
		return _subgridid;
	}
	public void set_subgridid(String _subgridid) {
		this._subgridid = _subgridid;
	}
	public int get_npage() {
		return _npage;
	}
	public void set_npage(int _npage) {
		this._npage = _npage;
	}
	public int get_totalrows() {
		return _totalrows;
	}
	public void set_totalrows(int _totalrows) {
		this._totalrows = _totalrows;
	}
	public String get_sidxcol() {
	//	return _sidxcol;
		return _sidx != null ? _sidx.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase() : _sidxcol;
	}
	public void set_sidxcol(String _sidxcol) {
		this._sidxcol = _sidxcol;
	}
	
	
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
	
	
	/**
	 * jqGrid 에서 "id" 라는 컬럼 name 사용할 수 없음 - 원천봉쇄
	 */
	
	private Object id;

	public final Object getId() {
		return id;
	}
	public final void setId(Object id) {
		this.id = id;
	}
}
