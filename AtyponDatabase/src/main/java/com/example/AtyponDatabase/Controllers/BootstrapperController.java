package com.example.AtyponDatabase.Controllers;

import com.example.AtyponDatabase.BootstrapperData;
import com.example.AtyponDatabase.Managers.ClusterManager;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/bootstrapper")
@RestController
public class BootstrapperController {

    @PostMapping("")
    public void getInitData(@RequestBody BootstrapperData initData){
        System.out.println(initData);
        ClusterManager.getInstance().setNodes(initData.getPorts());
    }
}