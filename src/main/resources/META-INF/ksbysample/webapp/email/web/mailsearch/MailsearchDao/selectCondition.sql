select
  /*%expand "em" */*
from
  email em
where
  /*%if mailsearchForm.toAddr != null */
  em.to_addr like /* @infix(mailsearchForm.toAddr) */'%t%'
  /*%end*/
  /*%if mailsearchForm.subject != null */
  and em.subject like /* @infix(mailsearchForm.subject) */'%スト%'
  /*%end*/
  /*%if mailsearchForm.name != null */
  and em.name like /* @infix(mailsearchForm.name) */'%花%'
  /*%end*/
  /*%if mailsearchForm.type != null */
  and em.type in /* mailsearchForm.type */('1', '3')
  /*%end*/
order by em.to_addr
