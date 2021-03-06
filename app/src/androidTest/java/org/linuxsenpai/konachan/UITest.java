package org.linuxsenpai.konachan;

import android.content.Context;
import android.net.wifi.WifiManager;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linuxsenpai.konachan.activity.MainActivity;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UITest {

	@Rule
	public ActivityScenarioRule<MainActivity> activityRule
			= new ActivityScenarioRule<>(MainActivity.class);
	private String stringToBetyped;

	@Before
	public void initValidString() {
		// Specify a valid string.
		stringToBetyped = "short_hair";
	}


	@Test
	public void NavigatePost_OrientationChange() {
		onView(withId(R.id.recycler_view)).perform(swipeUp());
		onView(withId(R.id.recycler_view)).perform(swipeUp());
		onView(withId(R.id.recycler_view)).perform(swipeUp());
		onView(withId(R.id.recycler_view)).perform(swipeUp());
	}

	@Test
	public void SingleViewSwipeNextImageTransition() {
		onView(withId(R.id.recycler_view)).perform(swipeUp());
		onView(withId(R.id.recycler_view)).perform(swipeUp());
		onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
		onView(isRoot()).perform(swipeRight(), swipeRight(), swipeRight(), swipeRight(), swipeRight(), swipeRight());
	}

	public void CloseOnBack() {

	}


	@Test
	public void searchText_selectSuggestion_valid() {
		onView(withId(R.id.action_searchview)).perform(click());
		onView(withId(R.id.action_searchview)).perform(typeText(stringToBetyped), closeSoftKeyboard());
		onData(anything()).atPosition(0).check(matches(hasDescendant(withText(stringToBetyped)))).perform(click());
	}

	@Test
	public void ImageInformation() {
		onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()), swipeUp());    //TODO resolve
		onView(withId(R.id.action_information)).perform(click());
		onView(withId(R.id.fragment_information_view)).perform(RecyclerViewActions.scrollToPosition(3));
	}

	@Test
	public void TagItemInvokesSearchView() {
		onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
		onView(withId(R.id.fragment_single_view)).perform(swipeUp());
		onView(withId(R.id.fragment_list_tags)).perform(swipeUp());
		onView(withId(R.id.fragment_list_tags)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
		onView(withId(R.id.recycler_view)).perform(swipeUp());
	}

	@Test
	public void TagItemInvokeWikiView() {
		onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
		onView(withId(R.id.fragment_single_view)).perform(swipeUp());
		onView(withId(R.id.fragment_list_tags)).perform(swipeUp());
		onView(withId(R.id.fragment_list_tags)).perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));
		onView(withId(R.id.recycler_view)).perform(swipeUp());
	}

	@Test
	public void OpenPreferenceFromAnywhere() {
		onView(withId(R.id.action_settings)).perform(click());
		onView(isRoot()).perform(pressBack());

		onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
		onView(withId(R.id.action_settings)).perform(click());
		onView(isRoot()).perform(pressBack(), pressBack());
	}

	@Test
	public void RefreshPostView() {
		onView(withId(R.id.swiperefresh)).perform(swipeDown());
	}

	@Test
	public void ScrollImageViewAsWhole() {
		onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollToPosition(0));
		onView(isRoot()).perform(swipeUp());
		onView(isRoot()).perform(swipeUp());
	}

	@Test
	public void NavigateFavorite() {
		onView(withId(R.id.pager)).perform(swipeLeft(), swipeLeft(), swipeLeft());
		//TODO add assert to check if this is the favorite fragment on the current pager.
		//onView(withId(R.id.pager)).check(matches(is))
	}

	@Test
	public void NavigateHistory() {
		onView(withId(R.id.pager)).perform(swipeLeft(), swipeLeft(), swipeLeft(), swipeLeft());

	}

	@Test
	public void NavigateWikiPage() {
		onView(withId(R.id.pager)).perform(swipeLeft(), swipeLeft());
	}

	@Test
	public void searchText_CloseSearchViewOnQuery() {
		onView(withId(R.id.action_searchview)).perform(click());
		onView(withId(R.id.action_searchview))
				.perform(typeText(stringToBetyped), closeSoftKeyboard());
		onView(withId(R.id.action_searchview)).check(matches(isDisplayed()));
		onView(withId(R.id.action_searchview)).perform();
		onView(withId(R.id.action_searchview)).check(matches(not(isDisplayed())));
	}

	@Test
	public void SearchView_SelectSuggestion() {

	}

	@Test
	public void SetImageFavorite() {

	}

	@Test
	public void TabNavigation() {

	}

	@Test
	public void SelectWikiPage() {

	}

	@Test
	public void SetImageBackgroundImage() {

	}

	@Test
	public void ClearCache() {
		onView(withId(R.id.tab_layout)).perform(click());
	}

	@Test
	public void NavigateAndSelectImage() {
		onView(withId(R.id.recycler_view)).perform(swipeUp());
		onView(withId(R.id.recycler_view)).perform(swipeUp());
		onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollToPosition(35));
	}

	@Test
	public void OnInternetLost() {
		onView(withId(R.id.action_searchview)).perform(typeText(stringToBetyped), closeSoftKeyboard());

		Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
		WifiManager wifi = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
		wifi.setWifiEnabled(false);

	}

	@Test
	public void PreferenceChangeState() {

	}

	@Test
	public void ZoomableImageView() {

	}
}
