package com.gensi.manage.entity;

import java.io.Serializable;

public class Gsrequest extends GsrequestKey implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column gensi..gsrequest.intime
     *
     * @mbggenerated Wed Feb 07 17:06:08 CST 2018
     */
    private String intime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column gensi..gsrequest.rsptime
     *
     * @mbggenerated Wed Feb 07 17:06:08 CST 2018
     */
    private String rsptime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column gensi..gsrequest.sysId
     *
     * @mbggenerated Wed Feb 07 17:06:08 CST 2018
     */
    private String sysid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table gensi..gsrequest
     *
     * @mbggenerated Wed Feb 07 17:06:08 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column gensi..gsrequest.intime
     *
     * @return the value of gensi..gsrequest.intime
     *
     * @mbggenerated Wed Feb 07 17:06:08 CST 2018
     */
    public String getIntime() {
        return intime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column gensi..gsrequest.intime
     *
     * @param intime the value for gensi..gsrequest.intime
     *
     * @mbggenerated Wed Feb 07 17:06:08 CST 2018
     */
    public void setIntime(String intime) {
        this.intime = intime == null ? null : intime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column gensi..gsrequest.rsptime
     *
     * @return the value of gensi..gsrequest.rsptime
     *
     * @mbggenerated Wed Feb 07 17:06:08 CST 2018
     */
    public String getRsptime() {
        return rsptime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column gensi..gsrequest.rsptime
     *
     * @param rsptime the value for gensi..gsrequest.rsptime
     *
     * @mbggenerated Wed Feb 07 17:06:08 CST 2018
     */
    public void setRsptime(String rsptime) {
        this.rsptime = rsptime == null ? null : rsptime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column gensi..gsrequest.sysId
     *
     * @return the value of gensi..gsrequest.sysId
     *
     * @mbggenerated Wed Feb 07 17:06:08 CST 2018
     */
    public String getSysid() {
        return sysid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column gensi..gsrequest.sysId
     *
     * @param sysid the value for gensi..gsrequest.sysId
     *
     * @mbggenerated Wed Feb 07 17:06:08 CST 2018
     */
    public void setSysid(String sysid) {
        this.sysid = sysid == null ? null : sysid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gensi..gsrequest
     *
     * @mbggenerated Wed Feb 07 17:06:08 CST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", intime=").append(intime);
        sb.append(", rsptime=").append(rsptime);
        sb.append(", sysid=").append(sysid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}