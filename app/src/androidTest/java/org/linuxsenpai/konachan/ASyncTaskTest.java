package org.linuxsenpai.konachan;

import android.content.Context;
import android.widget.ImageView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.linuxsenpai.konachan.tasks.DownloadImageViewTask;

@RunWith(AndroidJUnit4.class)
public class ASyncTaskTest {


	@Test
	public void Download_NoThrow() {

	}

	@Test
	public void DownloadJson_NoThrow() {

	}

	@Test
	public void LoadTags_download() {

	}


	@Test
	public void LoadImage_download() {
		Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
	}
}
