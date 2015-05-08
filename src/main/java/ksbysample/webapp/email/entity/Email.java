package ksbysample.webapp.email.entity;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

/**
 */
@Entity
@Table(name = "email")
public class Email {

    /** */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_id")
    Long emailId;

    /** */
    @Column(name = "from_addr")
    String fromAddr;

    /** */
    @Column(name = "to_addr")
    String toAddr;

    /** */
    @Column(name = "subject")
    String subject;

    /** */
    @Column(name = "name")
    String name;

    /** */
    @Column(name = "sex")
    String sex;

    /** */
    @Column(name = "type")
    String type;

    /** */
    @Column(name = "naiyo")
    String naiyo;

    /** 
     * Returns the emailId.
     * 
     * @return the emailId
     */
    public Long getEmailId() {
        return emailId;
    }

    /** 
     * Sets the emailId.
     * 
     * @param emailId the emailId
     */
    public void setEmailId(Long emailId) {
        this.emailId = emailId;
    }

    /** 
     * Returns the fromAddr.
     * 
     * @return the fromAddr
     */
    public String getFromAddr() {
        return fromAddr;
    }

    /** 
     * Sets the fromAddr.
     * 
     * @param fromAddr the fromAddr
     */
    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
    }

    /** 
     * Returns the toAddr.
     * 
     * @return the toAddr
     */
    public String getToAddr() {
        return toAddr;
    }

    /** 
     * Sets the toAddr.
     * 
     * @param toAddr the toAddr
     */
    public void setToAddr(String toAddr) {
        this.toAddr = toAddr;
    }

    /** 
     * Returns the subject.
     * 
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /** 
     * Sets the subject.
     * 
     * @param subject the subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /** 
     * Returns the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /** 
     * Sets the name.
     * 
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /** 
     * Returns the sex.
     * 
     * @return the sex
     */
    public String getSex() {
        return sex;
    }

    /** 
     * Sets the sex.
     * 
     * @param sex the sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /** 
     * Returns the type.
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }

    /** 
     * Sets the type.
     * 
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /** 
     * Returns the naiyo.
     * 
     * @return the naiyo
     */
    public String getNaiyo() {
        return naiyo;
    }

    /** 
     * Sets the naiyo.
     * 
     * @param naiyo the naiyo
     */
    public void setNaiyo(String naiyo) {
        this.naiyo = naiyo;
    }
}