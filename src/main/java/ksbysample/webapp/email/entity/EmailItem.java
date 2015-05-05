package ksbysample.webapp.email.entity;

import org.seasar.doma.*;

/**
 */
@Entity
@Table(name = "email_item")
public class EmailItem {

    /** */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_item_id")
    Long emailItemId;

    /** */
    @Column(name = "email_id")
    Long emailId;

    /** */
    @Column(name = "item")
    Short item;

    /** 
     * Returns the emailItemId.
     * 
     * @return the emailItemId
     */
    public Long getEmailItemId() {
        return emailItemId;
    }

    /** 
     * Sets the emailItemId.
     * 
     * @param emailItemId the emailItemId
     */
    public void setEmailItemId(Long emailItemId) {
        this.emailItemId = emailItemId;
    }

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
     * Returns the item.
     * 
     * @return the item
     */
    public Short getItem() {
        return item;
    }

    /** 
     * Sets the item.
     * 
     * @param item the item
     */
    public void setItem(Short item) {
        this.item = item;
    }
}