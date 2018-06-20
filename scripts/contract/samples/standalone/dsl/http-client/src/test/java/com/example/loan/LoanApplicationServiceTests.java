package com.example.loan;

import com.example.loan.model.Client;
import com.example.loan.model.LoanApplication;
import com.example.loan.model.LoanApplicationResult;
import com.example.loan.model.LoanApplicationStatus;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.RequestSpecification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

// tag::autoconfigure_stubrunner[]
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = {"com.example:http-server-dsl:+:stubs:6565"},
		stubsMode = StubRunnerProperties.StubsMode.LOCAL)
@DirtiesContext
public class LoanApplicationServiceTests {
// end::autoconfigure_stubrunner[]

	@Autowired
	private LoanApplicationService service;

	@Test
	public void shouldSuccessfullyApplyForLoan() {
		// given:
		LoanApplication application = new LoanApplication(new Client("1234567890"),
				123.123);
		// when:
		LoanApplicationResult loanApplication = service.loanApplication(application);
		// then:
		assertThat(loanApplication.getLoanApplicationStatus())
				.isEqualTo(LoanApplicationStatus.LOAN_APPLIED);
		assertThat(loanApplication.getRejectionReason()).isNull();
	}

	// tag::client_tdd[]
	@Test
	public void shouldBeRejectedDueToAbnormalLoanAmount() {
		// given:
		LoanApplication application = new LoanApplication(new Client("1234567890"),
				99999);
		// when:
		LoanApplicationResult loanApplication = service.loanApplication(application);
		// then:
		assertThat(loanApplication.getLoanApplicationStatus())
				.isEqualTo(LoanApplicationStatus.LOAN_APPLICATION_REJECTED);
		assertThat(loanApplication.getRejectionReason()).isEqualTo("Amount too high");
	}
	// end::client_tdd[]

	@Test
	public void shouldSuccessfullyGetAllFrauds() {
		// when:
		int count = service.countAllFrauds();
		// then:
		assertThat(count).isEqualTo(200);
	}

	@Test
	public void shouldSuccessfullyGetAllDrunks() {
		// when:
		int count = service.countDrunks();
		// then:
		assertThat(count).isEqualTo(100);
	}

	@Test
	public void shouldSuccessfullyGetCookies() {
		// when:
		String cookies = service.getCookies();
		// then:
		assertThat(cookies).isEqualTo("foo bar");
	}

	@Test
	public void shouldSuccessfullyWorkWithMultipart() {
		// given:
		RequestSpecification request = RestAssured.given()
				.baseUri("http://localhost:6565/")
				.header("Content-Type", "multipart/form-data")
				.multiPart("file1", "filename1", "content1".getBytes())
				.multiPart("file2", "filename1", "content2".getBytes())
				.multiPart("test", "filename1", "{\n  \"status\": \"test\"\n}".getBytes(), "application/json");

		// when:
		ResponseOptions response = RestAssured.given().spec(request)
				.post("/tests");

		// then:
		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
		DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
		assertThatJson(parsedJson).field("['status']").isEqualTo("ok");
	}

}
