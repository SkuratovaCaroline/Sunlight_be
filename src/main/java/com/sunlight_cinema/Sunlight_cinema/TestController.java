package com.sunlight_cinema.Sunlight_cinema;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {
    @GetMapping("/test")
    public String test() {
        return "Sunlight Cinema API is running!";
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> f112f53 (писание моих изменений)
