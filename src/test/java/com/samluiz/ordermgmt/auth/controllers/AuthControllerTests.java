package com.samluiz.ordermgmt.auth.controllers;

import com.samluiz.ordermgmt.auth.dtos.AuthDTO;
import com.samluiz.ordermgmt.auth.dtos.AuthRefreshDTO;
import com.samluiz.ordermgmt.auth.dtos.AuthResponseDTO;
import com.samluiz.ordermgmt.auth.services.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void login_ValidAuthDTO_ReturnsAuthenticatedUser() throws Exception {
        AuthDTO authDTO = AuthDTO.create("testUser", "testPassword");

        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setToken("testAccessToken");
        authResponseDTO.setRefreshToken("testRefreshToken");

        when(authService.authenticate(any(AuthDTO.class))).thenReturn(authResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .content("{\"username\":\"testUser\",\"password\":\"testPassword\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("testAccessToken"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.refresh_token").value("testRefreshToken"))
                .andDo(print());
    }

    @Test
    void refresh_ValidAuthRefreshDTO_ReturnsUpdatedToken() throws Exception {
        AuthRefreshDTO authRefreshDTO = new AuthRefreshDTO();
        authRefreshDTO.setRefreshToken("testRefreshToken");

        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setToken("testUpdatedAccessToken");
        authResponseDTO.setRefreshToken("testUpdatedRefreshToken");

        when(authService.refreshToken(eq("testRefreshToken"), any(), any())).thenReturn(authResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh")
                        .content("{\"refresh_token\":\"testRefreshToken\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("testUpdatedAccessToken"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.refresh_token").value("testUpdatedRefreshToken"))
                .andDo(print());
    }
}