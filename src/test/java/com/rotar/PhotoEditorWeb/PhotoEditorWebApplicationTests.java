package com.rotar.PhotoEditorWeb;


import com.rotar.PhotoEditorWeb.Models.Dto.UserDto;
import com.rotar.PhotoEditorWeb.controllers.AdminController;
import com.rotar.PhotoEditorWeb.controllers.HomeController;
import com.rotar.PhotoEditorWeb.controllers.PhotoController;
import com.rotar.PhotoEditorWeb.controllers.RestPhotoController;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.assertj.core.api.Assertions;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class PhotoEditorWebApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private HomeController homeController;

	@Autowired
	private AdminController adminController;

	@Autowired
	private PhotoController photoController;

	@Autowired
	private RestPhotoController restPhotoController;

	@Test
	void contextLoads() throws Exception {
		Assertions.assertThat(homeController).isNotNull();
	}

	@Test
	void testHomepage() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(
						containsString("HELLO TO HOMEPAGE!")
				));
	}

	@Test
	void testRedirection() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/welcome"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
	}

	@Test
	void testCorrectLogin() throws Exception {
		this.mockMvc.perform(SecurityMockMvcRequestBuilders.formLogin()
				.user("Petr Petrov").password("2222"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/welcome"));
	}


}
