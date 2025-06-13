package com.example.ActivityExample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToProfile(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    public void goToSkills(View view) {
        startActivity(new Intent(this, SkillsActivity.class));
    }

    public void goToProjects(View view) {
        startActivity(new Intent(this, ProjectsActivity.class));
    }

    public void goToHobbies(View view) {
        startActivity(new Intent(this, HobbiesActivity.class));
    }

    public void goToContact(View view) {
        startActivity(new Intent(this, ContactActivity.class));
    }
}
