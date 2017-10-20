package mokpoharbor.ringring;

/**
 * Created by pingrae on 2017. 10. 20..
 */
import java.text.Collator;
import java.util.Comparator;

public class ListData {
    /**
     * 리스트 정보를 담고 있을 객체 생성
     */
    // 아이콘
    public String mTitle;

    // 제목
    public String mText;

    // 날짜
    public String mDate;

    /**
     * 알파벳 이름으로 정렬
     */
    public static final Comparator<ListData> ALPHA_COMPARATOR = new Comparator<ListData>() {
        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(ListData mListDate_1, ListData mListDate_2) {
            return sCollator.compare(mListDate_1.mText, mListDate_2.mText);
        }
    };
}
