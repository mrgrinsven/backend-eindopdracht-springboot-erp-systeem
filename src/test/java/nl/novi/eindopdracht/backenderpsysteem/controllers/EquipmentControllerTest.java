package nl.novi.eindopdracht.backenderpsysteem.controllers;

import com.jayway.jsonpath.JsonPath;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class EquipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateEquipment() throws Exception {
        //Arrange

        String requestJson = """
                {
                   "name": "Test machine"
                }
                """;

        //Act
        MvcResult result = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/equipments")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        //Assert
        String responseBody = result.getResponse().getContentAsString();
        String createdId = JsonPath.read(responseBody, "$.id").toString();

        assertThat(result.getResponse().getHeader("Location"), matchesPattern("^.*/equipments/" + createdId));
    }

    @Test
    void shouldGetAllEquipments() throws Exception {
        // Act
        MvcResult result = this.mockMvc
                .perform(MockMvcRequestBuilders.get("/equipments"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        //Assert
        int status = result.getResponse().getStatus();
        assertThat(status, Matchers.is(200));
    }
}