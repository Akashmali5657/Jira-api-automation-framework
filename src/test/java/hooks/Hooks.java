package hooks;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.ScenarioContext;

public class Hooks {

    @Before
    public void beforeScenario(Scenario sc) {
        ScenarioContext.scenario = sc;
    }
}
