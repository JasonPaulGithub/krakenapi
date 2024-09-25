package com.example.demo.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class SiteInfo {
    String id;
    String name;
    ArrayList<Device> devices;
}

