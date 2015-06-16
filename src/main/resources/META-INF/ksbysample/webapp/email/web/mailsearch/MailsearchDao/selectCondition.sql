select
  /*%expand "em" */*
from
  email em
where
  /*%if mailsearchForm.toAddr != null && mailsearchForm.toAddr != "" */
  em.to_addr like /* @infix(mailsearchForm.toAddr) */'%t%'
  /*%end*/
  /*%if mailsearchForm.subject != null && mailsearchForm.subject != "" */
  and em.subject like /* @infix(mailsearchForm.subject) */'%スト%'
  /*%end*/
  /*%if mailsearchForm.name != null && mailsearchForm.name != "" */
  and em.name like /* @infix(mailsearchForm.name) */'%花%'
  /*%end*/
  /*%if mailsearchForm.type != null && mailsearchForm.type.size() > 0 */
  and em.type in /* mailsearchForm.type */('1', '3')
  /*%end*/
order by em.to_addr
