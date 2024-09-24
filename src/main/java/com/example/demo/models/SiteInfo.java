package com.example.demo.models;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class SiteInfo {
    String id;
    String name;
    ArrayList<Device> devices;
}

