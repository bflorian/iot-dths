/**
 *  Copyright 2019 SmartThings
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
    definition (name: "Simulated Refrigerator V2 Compartment", namespace: "smartthings/testing", author: "SmartThings") {
        capability "Sensor"
        capability "Contact Sensor"
        capability "Temperature Measurement"
        capability "Thermostat Cooling Setpoint"

        command "open"
        command "close"
        command "setTemperature", ["number"]
    }

    tiles(scale: 2) {
        standardTile("contact", "device.contact", width: 2, height: 2) {
            state("closed", label:'${name}', icon:"st.contact.contact.closed", backgroundColor:"#00A0DC", action: "open")
            state("open", label:'${name}', icon:"st.contact.contact.open", backgroundColor:"#e86d13", action: "close")
        }
        controlTile("temperature", "device.temperature", "slider",
            sliderType: "HEATING",
            debouncePeriod: 1500,
            range: "(0..50)",
            width: 2, height: 2) {
            state "default", action:"setTemperature", label:'${currentValue}', backgroundColor: "#00A0DC"
        }
        controlTile("setpoint", "device.coolingSetpoint", "slider",
            sliderType: "COOLING",
            debouncePeriod: 1500,
            range: "(0..50)",
            width: 2, height: 2) {
            state "default", action:"setCoolingSetpoint", label:'${currentValue}', backgroundColor: "#00A0DC"
        }
    }
}

def installed() {
    sendEvent(name: "contact", value: "closed")
    sendEvent(name: "temperature", value: device.componentName == "freezer" ? 2 : 40, unit: 'F')
    sendEvent(name: "coolingSetpoint", value: device.componentName == "freezer" ? 2 : 40, unit: 'F')
    initialize()
}

def updated() {
    initialize()
}

def initialize() {
	if (device.componentName == 'cooler') {
        parent.setTemperature(device.currentValue('temperature'))
    }
}


def open() {
    sendEvent(name: "contact", value: "open")
    parent.doorOpen(device.deviceNetworkId)
}

def close() {
    sendEvent(name: "contact", value: "closed")
    parent.doorClosed(device.deviceNetworkId)
}

def setTemperature(value) {
    sendEvent(name: "temperature", value: value, unit: 'F')
    if (device.componentName == 'cooler') {
        parent.setTemperature(value)
    }
}

def setCoolingSetpoint(value) {
    sendEvent(name: "coolingSetpoint", value: value, unit: 'F')
    sendEvent(name: "temperature", value: value, unit: 'F')
}
