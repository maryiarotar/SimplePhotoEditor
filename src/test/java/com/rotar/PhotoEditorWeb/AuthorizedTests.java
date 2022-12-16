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
import org.springframework.security.test.context.support.WithUserDetails;
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
@WithUserDetails("Petr Petrov")
class AuthorizedTests {

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
    void testForbiddenPage() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/show_users"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testMyPage() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/mypage"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
