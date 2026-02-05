package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import io.restassured.response.Response;
import org.testng.Assert;
import utils.ConfigReader;
import utils.ExtentRequestAndResponse;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class UpdateStoryStatusSteps {

    CreateUserStoryInBacklog createUserStoryInBacklog = new CreateUserStoryInBacklog();
    AssignSprintSteps assignSprintSteps = new AssignSprintSteps();

    Response response;
    Properties prop = ConfigReader.getProperties();
    String apiToken = prop.getProperty("API_Token_base64");

    String issueKey;
    String transitionId;

    @Given("user create user story and it has issue key")
    public void userCreateUserStoryAndItHasIssueKey() {
        createUserStoryInBacklog.user_has_create_issue_payload();
        createUserStoryInBacklog.user_calls_create_issue_api_with_post_method();
        issueKey = createUserStoryInBacklog.response_should_contain_issue_key();

        assignSprintSteps.userHasSprintId();
        assignSprintSteps.userCallsAssignSprintAPI();

        System.out.println("issue Key is !!!! : " + issueKey +" and ");
    }

    @When("user fetches transitions for issue")
    public void user_fetches_transitions_for_issue() {

        response =
                given()
                        .header("Authorization", apiToken)
                        .header("Content-Type", "application/json")
                        .when()
                        .get("/rest/api/3/issue/" + issueKey + "/transitions");

        ExtentRequestAndResponse.logRequestAndResponse("get", "/rest/api/3/issue/" + issueKey + "/transitions",response);

        // Extract transitionId for Done status
        List<Map<String, Object>> transitions =
                response.jsonPath().getList("transitions");

        for (Map<String, Object> t : transitions) {
            Map<String, String> to = (Map<String, String>) t.get("to");

            if (to.get("name").equalsIgnoreCase("Done")) {
                transitionId = (String) t.get("id");
                break;
            }
        }

        System.out.println("Transition Id for Done: " + transitionId);
    }

    @When("user updates issue status to Done")
    public void user_updates_issue_status_to_done() {

        String requestBody = "{\n" +
                "  \"transition\": {\n" +
                "    \"id\": \"" + transitionId + "\"\n" +
                "  }\n" +
                "}";

        response =
                given()
                        .header("Authorization", apiToken)
                        .header("Content-Type", "application/json")
                        .body(requestBody)
                        .when().log().all()
                        .post("/rest/api/3/issue/" + issueKey + "/transitions");
        ExtentRequestAndResponse.logRequestAndResponse("post", requestBody + issueKey + "/transitions",response);
    }

    @Then("status code should be {int} .")
    public void statusCodeShouldBe(Integer expectedStatusCode) {
        Assert.assertEquals(response.getStatusCode(), 204);
        System.out.println("Status updated successfully.");
    }

}
