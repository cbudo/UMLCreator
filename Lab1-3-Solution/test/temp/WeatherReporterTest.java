package temp;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WeatherReporterTest {
	private IWeatherReporter reporter;

	@Before
	public void setUp() throws Exception {
		reporter = new WeatherReporter();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testAddSubscriber() {
		ISubscriber temperatureDisplay = new TemperatureDisplay();
		ISubscriber humidityDisplay = new HumidityDisplay();
	}

	@Test
	public final void testRemoveSubscriber() {
	}

	@Test
	public final void testSetData() {
	}

	@Test
	public final void testGetData() {
	}

}
