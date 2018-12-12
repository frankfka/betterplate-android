package app.betterplate.betterplate.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import app.betterplate.betterplate.R;
import app.betterplate.betterplate.adapter.MenuListAdapter;
import app.betterplate.betterplate.data.core.Food;
import app.betterplate.betterplate.data.core.Menu;
import app.betterplate.betterplate.data.core.Restaurant;
import app.betterplate.betterplate.service.DatabaseService;
import app.betterplate.betterplate.service.SortService;

public class RestaurantMenusActivity extends AppCompatActivity {

    //TODO make this class prettier

    public static String RESTAURANT_ID_KEY = "RESTAURANT_ID_KEY";
    public static String MENU_NAME_KEY = "MENU_NAME_KEY";
    public static String FOODS_KEY = "FOODS";
    private static String SORT_INC_CALORIES = "Calories (Low to High)";
    private static String SORT_DEC_PROTEIN =  "Protein (High to Low)";
    private static String SORT_INC_CARBS = "Carbohydrates (Low to High)";
    private static String SORT_INC_FAT = "Fat (Low to High)";
    private RestaurantMenuCollectionPagerAdapter restaurantMenuCollectionPagerAdapter;
    private ViewPager menuPager;
    private TabLayout menuTabs;
    private Toolbar toolbar;
    private Restaurant restaurant = null;
    private List<Food> allRestaurantFoods = null;
    private List<Menu> menus;
    private DatabaseService databaseService;
    private static final String LOGTAG = "RestaurantMenusActivity";
    private int restaurantId;
    private Spinner sortBySpinner;
    private final String[] items = new String[]{SORT_INC_CALORIES, SORT_DEC_PROTEIN, SORT_INC_CARBS, SORT_INC_FAT};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menus);

        databaseService = new DatabaseService(this);
        // Get Restaurant and their menus
        restaurantId = (int) getIntent().getExtras().get(RESTAURANT_ID_KEY);

        /**
         * Set up sortBySpinner
         */
        sortBySpinner = findViewById(R.id.sortBySpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.sort_spinner_item, items);
        sortBySpinner.setAdapter(spinnerAdapter);

        findViewById(R.id.currentMealButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CurrentMealActivity.class);
                startActivity(intent);
            }
        });

        try {
            restaurant = databaseService.getRestaurantFromId(restaurantId);
            menus = databaseService.getAllMenusFromRestaurant(restaurantId);

            // Try to add another menu
            Menu allItems = new Menu(Integer.MIN_VALUE, "All Items", restaurantId);
            allRestaurantFoods = databaseService.getAllFoodsFromRestaurant(restaurantId);
            menus.add(0, allItems);

        } catch (ExecutionException | InterruptedException e) {
            Log.e(LOGTAG, "Error retrieving menus from database for restaurant ID ".concat(String.valueOf(restaurantId)), e);
        }

        // Initialize view references
        restaurantMenuCollectionPagerAdapter =
                new RestaurantMenuCollectionPagerAdapter(
                        getSupportFragmentManager());
        menuPager = findViewById(R.id.restaurantMenuTabViewPager);
        menuTabs = findViewById(R.id.menuTabs);
        toolbar = findViewById(R.id.toolBar);

        // Populate all the views
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(restaurant.getName());
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // Set up menu
        menuPager.setAdapter(restaurantMenuCollectionPagerAdapter);
        menuTabs.setupWithViewPager(menuPager);
        menuTabs.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // When the tab is selected, switch to the
                // corresponding page in the ViewPager.
                menuPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                EditText menuSearch = findViewById(R.id.menuItemSearchBar);
                menuSearch.setText("");
                menuSearch.clearFocus();
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(menuSearch.getWindowToken(), 0);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selection = items[position];
                RestaurantMenuCollectionPagerAdapter newSortedAdapter = new RestaurantMenuCollectionPagerAdapter(getSupportFragmentManager());
                if(selection.equals(SORT_INC_CALORIES)) {
                    newSortedAdapter.setSortBy(SortService.SORT_BY_INC_CALORIES);
                } else if (selection.equals(SORT_DEC_PROTEIN)) {
                    newSortedAdapter.setSortBy(SortService.SORT_BY_DEC_PROTEIN);
                } else if (selection.equals(SORT_INC_CARBS)) {
                    newSortedAdapter.setSortBy(SortService.SORT_BY_INC_CARBS);
                } else if (selection.equals(SORT_INC_FAT)) {
                    newSortedAdapter.setSortBy(SortService.SORT_BY_INC_FAT);
                }
                menuPager.setAdapter(newSortedAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    // Allow for back navigation using the toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // Returns the relevant fragment
    public class RestaurantMenuCollectionPagerAdapter extends FragmentStatePagerAdapter {
        private RestaurantMenuFragment fragment;
        private int sortBy = 0;

        public RestaurantMenuCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            fragment = new RestaurantMenuFragment();
            Bundle bundle = new Bundle();
            List<Food> foodsInMenu = null;
            bundle.putString(MENU_NAME_KEY, menus.get(position).getName());
            if (position == 0) {
                foodsInMenu = allRestaurantFoods;
            } else {
                try {
                    foodsInMenu = new ArrayList<>(databaseService.getFoodFromMenuId(menus.get(position).getId()));
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(LOGTAG, "Error retrieving foods from menu ID: ".concat(String.valueOf(menus.get(position).getId())), e);
                }
            }
            foodsInMenu = SortService.sortFoods(foodsInMenu, sortBy);
            bundle.putSerializable(FOODS_KEY, (Serializable) foodsInMenu);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return menus.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return menus.get(position).getName();
        }

        public void setSortBy(int sortBy) {
            this.sortBy = sortBy;
        }

    }

    // This represents a fragment - in our case, a list of menu items
    public static class RestaurantMenuFragment extends Fragment {

        private MenuListAdapter menuListAdapter;
        private List<Food> foods;
        private RecyclerView menuCategoryList;

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 final ViewGroup container, Bundle savedInstanceState) {

            // This inflates the list of menus
            final View rootView = inflater.inflate(
                    R.layout.menu_item_list, container, false);

            // This is the list of menus
            menuCategoryList = rootView.findViewById(R.id.menuCategoryList);
            menuCategoryList.setHasFixedSize(true);
            menuCategoryList.setLayoutManager(new LinearLayoutManager(getActivity()));

            foods = (List<Food>) getArguments().getSerializable(FOODS_KEY);
            // Send the Menu ID to the adapter
            menuListAdapter = new MenuListAdapter(foods);

            final EditText searchBar = rootView.findViewById(R.id.menuItemSearchBar);
            final ImageButton clearTextButton = rootView.findViewById(R.id.clearSearchButton);
            searchBar.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String input = searchBar.getText().toString();
                    if (input.isEmpty()) {
                        clearTextButton.setVisibility(View.GONE);
                    } else {
                        clearTextButton.setVisibility(View.VISIBLE);
                    }
                    menuListAdapter.filter(input);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            searchBar.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        searchBar.clearFocus();
                        InputMethodManager inputManager = (InputMethodManager)
                                getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.toggleSoftInput(0, 0);
                        return true;
                    }
                    return false;
                }
            });
            clearTextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchBar.setText("");
                }
            });

            menuCategoryList.setAdapter(menuListAdapter);

            return rootView;
        }
    }


}
