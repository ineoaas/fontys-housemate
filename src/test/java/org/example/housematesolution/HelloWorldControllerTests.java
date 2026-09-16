package org.example.housematesolution;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HelloWorldController.class)
class HelloWorldControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void helloWorldReturnsGreeting() throws Exception {
        mockMvc.perform(get("/hello-world"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"));
    }

}
