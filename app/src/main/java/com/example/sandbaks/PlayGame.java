package com.example.sandbaks;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;


public class PlayGame extends AppCompatActivity {
    public static Button stone, bronze, iron, spanish, american, japan, self, itemRecipe;
    ImageButton logOut;
    private static boolean openSidebar;
    private String currentMenu = "None";
    static LinearLayout sidebar;
    public static TextView goalText, tutorialTitle, tutorialDescription, tapScreen;
    VideoView video;
    RelativeLayout tutorialLayout;
    private int currentTutorial = 1;

    DBHelper db = new DBHelper();

    // menu setup
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_play, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playgame);
        setupRecyclerView();
        init();
        EraUnlocker.unlock();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        if (item.getItemId() == R.id.homepage) {
            Toast.makeText(this, "back to homepage", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (item.getItemId() == R.id.volume){
            Toast.makeText(this, "adjust volume", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (item.getItemId() == R.id.findElement) {
            Toast.makeText(this, "find an unlocked element", Toast.LENGTH_SHORT).show();
            return true;
        }

            else super.onOptionsItemSelected(item);
        return false;
    }


    public void init() {
        goalText = findViewById(R.id.goalText);
        video = findViewById(R.id.tutorialVideo);
        tutorialLayout = findViewById(R.id.tutorialLayout);
        tutorialLayout.setVisibility(View.INVISIBLE);

        tutorialTitle = findViewById(R.id.tutorialTitle);
        tutorialDescription = findViewById(R.id.tutorialDescription);

        tapScreen = findViewById(R.id.tapScreen);
        tapScreen.setVisibility(View.INVISIBLE);

        loadFragments();
        setupClickListeners();

        db.initDB(this);


        showTutorial(currentTutorial);
    }

    void showTutorial(int tutorialNumber) {
        ArrayList<String> items;
        items = Utils.getItemsFromString(db.getStoneItems(Utils.userID));
        if(items.size() > 7){
            return;
        }

        switch (tutorialNumber) {
            case 1:
                showSpecificTutorial(R.raw.drag_element, "Place Elements on Screen",
                        "Hold then Drag Elements from Sidebar to screen.");
                break;
            case 2:
                showSpecificTutorial(R.raw.combine_elements, "Combine Elements",
                        "Hold then Drag Elements from Sidebar to another element on the screen.");
                break;
            case 3:
                showSpecificTutorial(R.raw.remove_element, "Remove Element",
                        "Hold then Drag Elements from screen to the rightmost/leftmost side of the screen.");
                break;
            case 4:
                showSpecificTutorial(R.raw.drag_screen, "Drag Screen",
                        "Swipe the screen vertically to show more space/slot for the elements.");
                break;

            default:
                tutorialLayout.setVisibility(View.INVISIBLE);
                break;
        }
    }




    private void loadFragments() {
        // Load all fragments initially
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, StoneFragment.class, null)
                .addToBackStack(null)
                .commit();

        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, BronzeFragment.class, null)
                .addToBackStack(null)
                .commit();

        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, IronFragment.class, null)
                .addToBackStack(null)
                .commit();

        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, SpanishFragment.class, null)
                .addToBackStack(null)
                .commit();

        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, AmericanFragment.class, null)
                .addToBackStack(null)
                .commit();

        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, JapanFragment.class, null)
                .addToBackStack(null)
                .commit();

        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, SelfFragment.class, null)
                .addToBackStack(null)
                .commit();
    }

    private void setupClickListeners() {
        // Set up click listeners for the buttons
        stone = findViewById(R.id.btnStone);
        stone.setOnClickListener(v -> {
            showFragment(StoneFragment.class);
            sideBar("Stone");
        });

        bronze = findViewById(R.id.btnBronze);
        bronze.setOnClickListener(v -> {
            showFragment(BronzeFragment.class);
            sideBar("Bronze");
        });
        bronze.setVisibility(View.INVISIBLE);

        iron = findViewById(R.id.btnIron);
        iron.setOnClickListener(v -> {
            showFragment(IronFragment.class);
            sideBar("Iron");
        });
        iron.setVisibility(View.INVISIBLE);

        spanish = findViewById(R.id.btnSpanish);
        spanish.setOnClickListener(v -> {
            showFragment(SpanishFragment.class);
            sideBar("Spanish");
        });
        spanish.setVisibility(View.INVISIBLE);

        american = findViewById(R.id.btnAmerica);
        american.setOnClickListener(v -> {
            showFragment(AmericanFragment.class);
            sideBar("American");
        });
        american.setVisibility(View.INVISIBLE);

        japan = findViewById(R.id.btnJapan);
        japan.setOnClickListener(v -> {
            showFragment(JapanFragment.class);
            sideBar("Japan");
        });
        japan.setVisibility(View.INVISIBLE);

        self = findViewById(R.id.btnSelf);
        self.setOnClickListener(v -> {
            showFragment(SelfFragment.class);
            sideBar("Self");
        });
        self.setVisibility(View.INVISIBLE);

        itemRecipe = findViewById(R.id.recipeButton);
        itemRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRecipes();
            }
        });

        logOut = findViewById(R.id.logOutButton);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.userName = "Guest";
                logOut();
            }
        });
    }

    void openRecipes(){
        Intent intent = new Intent(this, ItemRecipes.class);
        startActivity(intent);
    }

    void logOut(){
        Utils.itemsOnScreen = new ArrayList<>();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showFragment(Class<? extends Fragment> fragmentClass) {
        try {
            Fragment fragment = fragmentClass.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, fragment, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void sideBar(String newMenu){
        if (currentMenu.equalsIgnoreCase("None")){
            openOrClose();;
        }

        else {
            if(currentMenu.equalsIgnoreCase(newMenu)){
                openOrClose();
            }

            else{
                if(!openSidebar){
                    openSideBar();;
                }
            }
        }
        currentMenu = newMenu;
    }



    public void openOrClose(){
        if (openSidebar) {
            closeSideBar();
        } else {
            openSideBar();
        }
    }

    public void openSideBar() {
        sidebar = findViewById(R.id.sidebarButtons);
        final int endMargin = 300;

        updateMargin(endMargin);
        openSidebar = true;
    }


    public static void closeSideBar() {

        if (!PlayGame.openSidebar){
            return;
        }

        sidebar = PlayGame.sidebar.findViewById(R.id.sidebarButtons);
        final int startMargin = 300;
        final int endMargin = 0;

        ValueAnimator animator = ValueAnimator.ofInt(startMargin, endMargin);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                updateMargin(animatedValue);
            }
        });
        animator.setDuration(300);
        animator.start();

        PlayGame.openSidebar = false;
    }


    private static void updateMargin(int margin) {
        ViewGroup.MarginLayoutParams sidebarLayoutParams = (ViewGroup.MarginLayoutParams) sidebar.getLayoutParams();
        sidebarLayoutParams.setMarginStart(margin);
        sidebar.setLayoutParams(sidebarLayoutParams);
    }

    private void setupRecyclerView() {
        RecyclerView dropArea = findViewById(R.id.dropSpace);
        GridLayoutManager grid = new GridLayoutManager(this, 3);

        dropArea.setLayoutManager(grid);


        for(int i=0; i<300; i++){
            Utils.itemsOnScreen.add(
                    new ItemCards(
                            " ",
                            Utils.createEmptyBitmap()));
        }

        DropAreaAdapter adapter = new DropAreaAdapter(this, Utils.itemsOnScreen);

        dropArea.setAdapter(adapter);
    }

    void showSpecificTutorial(int videoRawId, String title, String description) {
        Uri uri = Utils.getVideoFromRaw(videoRawId);
        tutorialTitle.setText(title);
        tutorialDescription.setText(description);
        video.setVideoURI(uri);

        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                tutorialLayout.setVisibility(View.VISIBLE);
                video.start();
            }
        });

        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                video.seekTo(0);
                video.start();
                tapScreen.setVisibility(View.VISIBLE);
                tutorialLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tutorialLayout.setVisibility(View.INVISIBLE);
                        // Show the next tutorial on click
                        currentTutorial++;
                        showTutorial(currentTutorial);
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }
}