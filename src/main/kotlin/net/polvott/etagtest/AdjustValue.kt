package net.polvott.etagtest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AdjustValue {

    @GetMapping("/values/{newvalue}")
    fun setValue(@PathVariable newvalue: String): String {
        return "Adjusting value $newvalue\n"
    }

    @GetMapping("/values", produces = ["application/json"])
    fun getValue(): ResponseEntity<Any> {
        return ResponseEntity.ok()
            .body("{\"value\":\"This is the vales\"}")
    }
}
