/*
 *  Copyright 2015 sourcestream GmbH
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.sourcestream.movieDB;

import android.app.FragmentManager;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.test.InstrumentationTestCase;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import de.sourcestream.movieDB.controller.GalleryPreviewSlide;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21)
public class GalleryPreviewSlideTest extends InstrumentationTestCase {

    private static final String FRAGMENT_TAG = "fragment";
    private MainActivity activity;
    private GalleryPreviewSlide galleryPreviewSlide;


    /**
     * Adds the fragment to a new blank activity, thereby fully
     * initializing its view.
     */
    @Before
    public void setUp() throws Exception {
        galleryPreviewSlide = new GalleryPreviewSlide();
        activity = Robolectric.buildActivity(MainActivity.class).create().get();
        FragmentManager manager = activity.getFragmentManager();
        Bundle args = new Bundle();
        ArrayList<String> galleryList = new ArrayList<>();
        args.putStringArrayList("galleryList", galleryList);
        galleryPreviewSlide.setArguments(args);
        manager.beginTransaction().add(galleryPreviewSlide, FRAGMENT_TAG).commit();
    }

    @Test
    public void testPreconditions() throws Exception {
        assertNotNull("activity is null", activity);
        assertNotNull("DrawerLayout is null", activity.getMDrawerLayout());
        assertNotNull("galleryPreviewSlide is null", galleryPreviewSlide);
        assertNotNull("cant find fragment", activity.getFragmentManager().findFragmentByTag(FRAGMENT_TAG));
    }

    @Test
    public void testActionBar() throws Exception {
        boolean expected = false;
        assertNotNull("actionBar is null", activity.getSupportActionBar());
        assertEquals("actionBar is not hidden!", expected, activity.getSupportActionBar().isShowing());
    }

    @Test
    public void testViews() throws Exception {
        View galleryPreviewSlideRoot = galleryPreviewSlide.getView();
        assertNotNull("galleryPreviewSlide rootView is null", galleryPreviewSlideRoot);
        ViewPager mViewPager = (ViewPager) galleryPreviewSlideRoot.findViewById(R.id.galleryPager);
        assertNotNull("mViewPager is null", mViewPager);
        int expected = 1;
        assertEquals("mViewPager offscreen limit is not 1!", expected, mViewPager.getOffscreenPageLimit());
        assertNotNull("mViewPager adapter is null", mViewPager.getAdapter());

    }


}
