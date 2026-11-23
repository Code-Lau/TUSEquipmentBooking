package com.example.tusequipmentbooking.data

import com.example.tusequipmentbooking.model.Category
import com.example.tusequipmentbooking.model.Equipment

val sampleCategories = listOf(
    Category(1, "Cameras"),
    Category(2, "Audio Equipment"),
    Category(3, "Laptops"),
    Category(4, "Computing Accessories"),
    Category(5, "Educational Disassembly"),
    Category(6, "Misc")
)

val sampleEquipments = listOf(
    Equipment(id = 10, categoryId = 1, name = "DSLR Camera",
        description = "Canon DSLR with 18-55mm lens, battery and charger."),
    Equipment(id = 20, categoryId = 1, name = "Action Camera",
        description = "Waterproof 4K action camera with mounts."),
    Equipment(id = 30, categoryId = 2, name = "USB Condenser Microphone",
        description = "Cardioid mic with desk stand and pop filter."),
    Equipment(id = 40, categoryId = 2, name = "Portable PA Speaker",
        description = "Battery powered speaker with wireless mic."),
    Equipment(id = 50, categoryId = 3, name = "Laptop (i7, 16GB)",
        description = "High spec laptop with CAD and video editing software."),
    Equipment(id = 60, categoryId = 3, name = "Laptop (i5, 8GB)",
        description = "Laptop with coding software."),
    Equipment(id = 70, categoryId = 4, name = "Graphics Tablet",
        description = "A4 drawing tablet with stylus."),
    Equipment(id = 80, categoryId = 4, name = "Wireless Keyboard",
        description = "Standard QWERTY keyboard with wireless connectivity."),
    Equipment(id = 90, categoryId = 5, name = "Educational: Dell Desktop",
        description = "Dell 2009 desktop tower for disassembly."),
    Equipment(id = 100, categoryId = 5, name = "Educational: Lenovo Thinkpad",
        description = "Standard Lenovo Thinkpad for disassembly"),
    Equipment(id = 110, categoryId = 6, name = "Raspberry Pi Kit",
        description = "Pi 4 with case, power supply and SD card."),
    Equipment(id = 120, categoryId = 6, name = "Arduino Kit",
        description = "Motherboard, bread board, wires and 5V power supply.")
)