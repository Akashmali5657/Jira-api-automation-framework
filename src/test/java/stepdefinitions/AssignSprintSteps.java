package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;
import utils.ConfigReader;
import utils.ExtentRequestAndResponse;

import java.util.Properties;

import static io.restassured.RestAssured.given;

public class AssignSprintSteps {

    CreateUserStoryInBacklog createUserStoryInBacklog = new CreateUserStoryInBacklog();

    Response response;
    Properties prop = ConfigReader.getProperties();

    String apiToken = prop.getProperty("API_Token_base64");

    // Example values (you can also store dynamically)
    String issueKey;
    String sprintId;

    @Given("user create user story and take issue key")
    public void userCreateUserStoryAndTakeIssueKey() {

        createUserStoryInBacklog.user_has_create_issue_payload();
        createUserStoryInBacklog.user_calls_create_issue_api_with_post_method();
        issueKey = createUserStoryInBacklog.response_should_contain_issue_key();
        System.out.println("issue Key is : " + issueKey);
    }

    @And("user has sprint id")
    public void userHasSprintId() {
        response =
                given()
                        .header("Authorization", apiToken)
                        .header("Content-Type", "application/json").log().all()
                        .when()
                        .get("rest/agile/1.0/board/1/sprint?state=active");

        sprintId = response.jsonPath().getString("values[0].id");
        System.out.println("Get Sprint id Response : " + response.jsonPath().getString("values[0].id"));
        ExtentRequestAndResponse.logRequestAndResponse("Get", "rest/agile/1.0/board/1/sprint?state=active",response);

    }

    @When("user calls assign sprint API")
    public void userCallsAssignSprintAPI() {
        response =
                given()
                        .header("Authorization", apiToken)
                        .header("Content-Type", "application/json")
                        .body("{\n" +
                                "  \"issues\": [\"" + issueKey + "\"]\n" +
                                "}")
                        .when()
                        .post("/rest/agile/1.0/sprint/" + sprintId + "/issue");
        System.out.println("sprint id @@@@ : " + sprintId);
        ExtentRequestAndResponse.logRequestAndResponse("Get", "/rest/agile/1.0/sprint/" + sprintId + "/issue",response);

    }

    @Then("three status code should be {int}")
    public void threeStatusCodeShouldBe(int arg0) {
        System.out.println("Assign Sprint Response Code: " + response.getStatusCode());
    }

    @And("update priority and label of user story")
    public void updatePriorityAndLabelOfUserStory() {

        AssignSprintSteps assignSprintSteps = new AssignSprintSteps();
        String issue_Id = assignSprintSteps.getActiveSprint();
        System.out.println("issue_Id : " + issue_Id);

        String requestBody = "{\n" +
                "  \"fields\": {\n" +
                "    \"priority\": {\n" +
                "      \"name\": \"Low\"\n" +
                "    },\n" +
                "        \"labels\": [\"api\", \"automation\", \"regression\"]\n" +
                "\n" +
                "  }\n" +
                "}";

        response =
                given()
                        .header("Authorization", apiToken)
                        .header("Content-Type", "application/json")
                        .body(requestBody)
                        .when().log().all()
                        .put("/rest/api/3/issue/"  + issue_Id+ "\n");

        Assert.assertEquals(response.getStatusCode(), 204, "Match status code");
        ExtentRequestAndResponse.logRequestAndResponse("Put", requestBody,response);
    }

    private String getActiveSprint(){
        response =
                given()
                        .header("Authorization", apiToken)
                        .header("Content-Type", "application/json")
                        .when()
                        .get("/rest/api/3/search/jql?jql=project=SCRUM AND issuetype=Story AND sprint in openSprints()");
        String issueId = response.jsonPath().getString("issues[0].id");
        return issueId;
    }
}
