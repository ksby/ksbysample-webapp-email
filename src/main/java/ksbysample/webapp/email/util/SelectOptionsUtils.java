package ksbysample.webapp.email.util;

import org.seasar.doma.jdbc.SelectOptions;
import org.springframework.data.domain.Pageable;

public class SelectOptionsUtils {

    public static SelectOptions get(Pageable pageable, boolean countFlg) {
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        int limit = pageable.getPageSize();

        SelectOptions selectOptions = null;
        if (countFlg) {
            selectOptions = SelectOptions.get().offset(offset).limit(limit).count();
        }
        else {
            selectOptions = SelectOptions.get().offset(offset).limit(limit);
        }

        return selectOptions;
    }
    
}