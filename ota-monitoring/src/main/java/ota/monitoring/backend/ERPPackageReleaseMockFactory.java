package ota.monitoring.backend;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

import ota.monitoring.backend.esb.PackageReleaseService;
import ota.monitoring.backend.esb.model.MockPackageData;
import ota.monitoring.shared.enums.PackageInstallmentType;

@Component
public class ERPPackageReleaseMockFactory {
	private static final Logger LOG = LoggerFactory.getLogger(ERPPackageReleaseMockFactory.class);

	public static final List<String> packageNames = Lists.newArrayList("autopilote", "infotainment", "navigation");
	public static final Map<String, List<String>> featuresOfPackage = Maps.newHashMap();
	private static final List<String> fleets = Lists.newArrayList("germany-spa", "swizerland-tda");
	private static final Random rand = new Random();

	@Autowired
	private PackageReleaseService packageReleaseService;

	/**
	 * Factory for creation of some mocked (dummy) data, which should be obtained
	 * from ERP system, and as such, here I simulate data generation every 1 second,
	 * for analitic purpose. <br />
	 * Usually, data from EPR requires some modification in order to be used by new
	 * system environment, hence MockPackageData transformation into ->
	 * PackageReleaseEvent, just to simulate that transforming process
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void startPackagesProduction() {
		LOG.info("Start producing simulated packages from ERP every 1 seconds ...");
		initERPMockedData();

		new Thread(() -> {
			int counter = 0;

			while (counter <= 20) {
				Function<Random, Integer> r = random -> random.nextInt() % 2 == 0 ? 0 : 1;

				MockPackageData mockedERPData;
				String fleetId = fleets.get(r.apply(rand));

				mockedERPData = checkForNewFeature(r, counter);
				LOG.info("Publishing new package from ERP ...");

				packageReleaseService.prepareDataForRelease(mockedERPData, fleetId);
				performLongRunningOperation();
				++counter;
			}
			LOG.info("Finished MOCK data production !!!");

		}).start();
		LOG.info("Production engaged !!!");

	}

	private void initERPMockedData() {
		List<String> autopiloteFeature = Lists.newArrayList("speed-limit", "time-limit-to-drive", "turn-on",
				"turn-off");
		List<String> infotainmentFeature = Lists.newArrayList("news", "message-alert", "display-visualisation");
		List<String> navigationFeature = Lists.newArrayList("select-city", "closests-route");

		featuresOfPackage.put("autopilote", autopiloteFeature);
		featuresOfPackage.put("infotainment", infotainmentFeature);
		featuresOfPackage.put("navigation", navigationFeature);
	}

	private MockPackageData checkForNewFeature(Function<Random, Integer> r, int counter) {
		PackageInstallmentType type = r.apply(rand) == 0 ? PackageInstallmentType.UPDATE
				: PackageInstallmentType.INSTALL;
		String packageName = packageNames.get(rand.nextInt(3));
		List<String> packageFeatureList = featuresOfPackage.get(packageName);
		String featureName = packageFeatureList.get(rand.nextInt(packageFeatureList.size()));
		String description = "Some description of feature " + featureName;
		Double version = Double.parseDouble((rand.nextInt(9) + 1) + "." + rand.nextInt(10));
		byte[] payload = (counter > 10 ? r.apply(rand) == 0 : true)
				? new String("Some instruction and data for installment").getBytes()
				: null;

		MockPackageData data = new MockPackageData(type, packageName, featureName, version, payload, description);

		return data;
	}

	private void performLongRunningOperation() {
		LOG.info("New Packages production in ERP in progress ...");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
