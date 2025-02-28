package com.ipms.policy.test.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipms.policy.controller.PolicyController;
import com.ipms.policy.entity.Policy;
import com.ipms.policy.service.PolicyService;

@ExtendWith(MockitoExtension.class)
public class PolicyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PolicyService policyService;

    @InjectMocks
    private PolicyController policyController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(policyController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllPolicies() throws Exception {
        List<Policy> policies = Arrays.asList(new Policy(), new Policy());
        when(policyService.findAll()).thenReturn(policies);

        mockMvc.perform(get("/api/policies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetPolicyById() throws Exception {
        Policy policy = new Policy();
        policy.setId(1L);
        when(policyService.findById(1L)).thenReturn(policy);

        mockMvc.perform(get("/api/policies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreatePolicy() throws Exception {
        Policy policy = new Policy();
        policy.setId(1L);
        when(policyService.save(any(Policy.class))).thenReturn(policy);

        mockMvc.perform(post("/api/policies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(policy)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Policy created successfully"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUpdatePolicy() throws Exception {
        Policy policy = new Policy();
        policy.setId(1L);
        when(policyService.save(any(Policy.class))).thenReturn(policy);

        mockMvc.perform(put("/api/policies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(policy)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Policy updated successfully"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeletePolicy() throws Exception {
        doNothing().when(policyService).delete(1L);

        mockMvc.perform(delete("/api/policies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Policy with ID 1 deleted successfully"));
    }
}
