package com.example.dailyup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class HomeFragment extends Fragment {

    private TextView todayDateText;
    private TextView todayQuoteText;
    private TextView todayAuthorText;
    private ImageButton shareButton;
    private ImageButton bookmarkButton;
    private Button categoriesButton;

    // 명언 데이터 (실제 앱에서는 데이터베이스나 API에서 가져옴)
    private String[] quotes = {
            "성공은 준비된 기회를 만나는 것이다.",
            "꿈을 포기하지 마라. 꿈이 없으면 희망도 없다.",
            "오늘 할 수 있는 일을 내일로 미루지 마라.",
            "실패는 성공의 어머니다.",
            "노력 없이는 아무것도 얻을 수 없다."
    };

    private String[] authors = {
            "벤자민 프랭클린",
            "월트 디즈니",
            "벤자민 프랭클린",
            "토마스 에디슨",
            "아인슈타인"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initViews(view);
        setupTodayDate();
        setupTodayQuote();
        setupClickListeners();

        return view;
    }

    private void initViews(View view) {
        todayDateText = view.findViewById(R.id.today_date_text);
        todayQuoteText = view.findViewById(R.id.today_quote_text);
        todayAuthorText = view.findViewById(R.id.today_author_text);
        shareButton = view.findViewById(R.id.share_button);
        bookmarkButton = view.findViewById(R.id.bookmark_button);
        categoriesButton = view.findViewById(R.id.categories_button);
    }

    private void setupTodayDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREAN);
        String today = dateFormat.format(new Date());
        todayDateText.setText(today);
    }

    private void setupTodayQuote() {
        Random random = new Random();
        int index = random.nextInt(quotes.length);
        todayQuoteText.setText(quotes[index]);
        todayAuthorText.setText("- " + authors[index]);
    }

    private void setupClickListeners() {
        shareButton.setOnClickListener(v -> {
            // 공유 기능 구현
            String shareText = todayQuoteText.getText().toString() + "\n" + todayAuthorText.getText().toString();
            // 실제 공유 인텐트 구현 필요
        });

        bookmarkButton.setOnClickListener(v -> {
            // 북마크 기능 구현
            // 데이터베이스에 저장하는 로직 필요
        });

        categoriesButton.setOnClickListener(v -> {
            // 카테고리 화면으로 이동
            ((MainActivity) getActivity()).replaceFragment(new CategoriesFragment());
        });
    }
}