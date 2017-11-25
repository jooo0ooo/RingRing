package mokpoharbor.ringring;

/**
 * Created by pingrae on 2017. 10. 20..
 */

import java.text.Collator;
import java.util.Comparator;

public class ListData {
    public String mTitle;
    public String mText;
    public String mDate;

    public static final Comparator<ListData> ALPHA_COMPARATOR = new Comparator<ListData>() {
        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(ListData mListDate_1, ListData mListDate_2) {
            return sCollator.compare(mListDate_1.mDate, mListDate_2.mDate);
            //과제 제출 기간으로 정렬
        }
    };
}