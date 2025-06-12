package com.example.dailyup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends Fragment {

    private RecyclerView categoriesRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        initViews(view);
        setupCategories();
        setupRecyclerView();

        return view;
    }

    private void initViews(View view) {
        categoriesRecyclerView = view.findViewById(R.id.categories_recycler_view);
    }

    private void setupCategories() {
        categoryList = new ArrayList<>();
        categoryList.add(new Category("자기계발", "💪", "#FF6B6B"));
        categoryList.add(new Category("관계", "❤️", "#4ECDC4"));
        categoryList.add(new Category("동기부여", "🔥", "#45B7D1"));
        categoryList.add(new Category("위안", "🌸", "#96CEB4"));
        categoryList.add(new Category("감사", "🙏", "#FFEAA7"));
        categoryList.add(new Category("성공", "🏆", "#DDA0DD"));
        categoryList.add(new Category("꿈", "⭐", "#98D8C8"));
        categoryList.add(new Category("행복", "😊", "#F7DC6F"));
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        categoriesRecyclerView.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(categoryList, category -> {
            // 카테고리 클릭 시 해당 카테고리의 명언 목록으로 이동
            CategoryQuotesFragment fragment = CategoryQuotesFragment.newInstance(category.getName());
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        categoriesRecyclerView.setAdapter(categoryAdapter);
    }

    // Category 데이터 클래스
    public static class Category {
        private String name;
        private String emoji;
        private String color;

        public Category(String name, String emoji, String color) {
            this.name = name;
            this.emoji = emoji;
            this.color = color;
        }

        public String getName() { return name; }
        public String getEmoji() { return emoji; }
        public String getColor() { return color; }
    }
}