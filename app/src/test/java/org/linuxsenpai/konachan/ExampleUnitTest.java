package org.linuxsenpai.konachan;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class  ExampleUnitTest {


	@Test
	public void addition_isCorrect() {
		assertEquals(4, 2 + 2);
	}


	@Test
	public void PostItem_fetching(){

	}

	@Test
	public void readStringFromContext_LocalizedString() {
		// Given a Context object retrieved from Robolectric...
		//context.getResources().getDimension(R.dimen.fab_margin);
//		ClassUnderTest myObjectUnderTest = new ClassUnderTest(context);

		// ...when the string is returned from the object under test...
//		String result = myObjectUnderTest.getHelloWorldString();

		// ...then the result should be the expected one.
		//assertThat(result).isEqualTo(FAKE_STRING);
	}


}
