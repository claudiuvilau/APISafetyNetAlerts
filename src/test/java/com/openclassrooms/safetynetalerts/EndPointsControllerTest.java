package com.openclassrooms.safetynetalerts;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.safetynetalerts.controller.EndPointsController;
import com.openclassrooms.safetynetalerts.dao.JsonDaoImplements;

@WebMvcTest(controllers = EndPointsController.class)
public class EndPointsControllerTest {

	 @Autowired
	    private MockMvc mockMvc;

	    @MockBean
	    private JsonDaoImplements jsonDaoImplements;

	    @Test
	    public void testGetpersonsOfStationAdultsAndChild() throws Exception {
	    	mockMvc.perform(get("/firestation?stationNumber=1"))
	            .andExpect(status().isOk());
	    }
	    
}
