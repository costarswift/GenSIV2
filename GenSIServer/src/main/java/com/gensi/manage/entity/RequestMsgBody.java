package com.gensi.manage.entity;

public class RequestMsgBody {
    /**
     * 接口服务标识id
     */
    private String sysId;

    private String loanId;
    private String idCard;
    private String userName;
    private String requestType;
    private String searchType;
    private String remark;

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RequestMsgBody [loanId=" + loanId + ", idCard=" + idCard + ", userName=" + userName + ", requestType="
                + requestType + ", searchType=" + searchType + ", remark=" + remark + ", "
                + "]";
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }
}
