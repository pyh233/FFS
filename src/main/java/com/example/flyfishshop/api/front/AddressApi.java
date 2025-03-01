package com.example.flyfishshop.api.front;

import com.example.flyfishshop.service.AddressService;
import com.example.flyfishshop.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/front/api/v1",produces = MediaType.APPLICATION_JSON_VALUE)
public class AddressApi {
    AddressService addressService;

    @Autowired
    public void setAddressService(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/province")
    public ResponseEntity<JsonResult> province() {
        return ResponseEntity.ok(JsonResult.success("success",addressService.findAllProvince()));
    }

    @GetMapping("/city")
    public ResponseEntity<JsonResult> city(Integer provinceId) {
        if(provinceId == null) {
            return ResponseEntity.badRequest().body(JsonResult.fail("请先选择省份"));
        }else {
            return ResponseEntity.ok(JsonResult.success("ok",addressService.findAllCity(provinceId)));
        }
    }

    @GetMapping("/area")
    public ResponseEntity<JsonResult> area(Integer cityId) {
        if(cityId == null) {
            return ResponseEntity.badRequest().body(JsonResult.fail("请先选择市区"));
        }else {
            return ResponseEntity.ok(JsonResult.success("ok",addressService.findAllArea(cityId)));
        }
    }

    @GetMapping("/street")
    public ResponseEntity<JsonResult> street(Integer areaId) {
        if(areaId == null) {
            return ResponseEntity.badRequest().body(JsonResult.fail("请先选择区域"));
        }else {
            return ResponseEntity.ok(JsonResult.success("ok",addressService.findAllStreet(areaId)));
        }
    }
}
