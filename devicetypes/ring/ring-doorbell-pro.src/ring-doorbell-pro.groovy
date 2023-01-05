/*
===============================================================================
 *  Copyright 2016 SmartThings
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy
 *  of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
===============================================================================
 *  Purpose: Ring Pro Device Type Handler (DTH) File
 *
 *  Filename: ring-doorbell-pro.groovy
 *
===============================================================================
 */
import static groovy.json.JsonOutput.*
metadata {
    definition (name: "Ring Doorbell Pro", namespace: "Ring", author: "smartthings", ocfDeviceType: "x.com.st.d.doorbell") {
        capability "webrtc"
        capability "Video Stream"
        capability "Video Camera"
        capability "Button"
        capability "Health Check"
        capability "Motion Sensor"
        capability "Sensor"

        command "startStream"
        command "stopStream"
        command "refresh"
        attribute "active", "string"            // false = Active;  true = Inactive
    }

    tiles (scale: 2){
        multiAttributeTile(name: "videoPlayer", type: "videoPlayer", width: 6, height: 4) {
            tileAttribute("device.camera", key: "CAMERA_STATUS") {
                attributeState("on", label: "Active", icon: "https://smartthings-device-icons.s3.amazonaws.com/ring/ring_ready_tile.png", backgroundColor: "#00A0DC", defaultState: true)
                attributeState("off", label: "Inactive", icon: "https://smartthings-device-icons.s3.amazonaws.com/ring/ring_ready_tile.png",  backgroundColor: "#ffffff")
                attributeState("restarting", label: "Connecting", icon: "https://smartthings-device-icons.s3.amazonaws.com/ring/ring_ready_tile.png", backgroundColor: "#00A0DC")
                attributeState("unavailable", label: "Unavailable", icon: "https://smartthings-device-icons.s3.amazonaws.com/ring/ring_ready_tile.png", action: "refresh", backgroundColor: "#cccccc")
            }
            tileAttribute("device.errorMessage", key: "CAMERA_ERROR_MESSAGE") {
                attributeState("errorMessage", label: "", value: "", defaultState: true)
            }
            tileAttribute("device.camera", key: "PRIMARY_CONTROL") {
                attributeState("on", label: "Ready", icon: "https://smartthings-device-icons.s3.amazonaws.com/ring/ring_ready_tile.png", backgroundColor: "#ffffff", defaultState: true)
                attributeState("off", label: "Inactive", icon: "https://smartthings-device-icons.s3.amazonaws.com/ring/ring_ready_tile.png", backgroundColor: "#ffffff")
                attributeState("restarting", label: "Connecting", icon: "https://smartthings-device-icons.s3.amazonaws.com/ring/ring_ready_tile.png", backgroundColor: "#00A0DC")
                attributeState("unavailable", label: "Unavailable", icon: "https://smartthings-device-icons.s3.amazonaws.com/ring/ring_ready_tile.png", backgroundColor: "#cccccc")
            }
            tileAttribute("device.startLive", key: "START_LIVE") {
                attributeState("live", action: "startStream", defaultState: true)
            }
            tileAttribute("device.stream", key: "STREAM_URL") {
                attributeState("activeURL", defaultState: true)
            }
        }
        standardTile("stream", "device.stream", width: 2, height: 2, decoration: "flat") {
            state "default", label: "Start Stream", action: "start", icon: "st.Electronics.electronics7"
        }
        standardTile("button", "device.button", width: 2, height: 2, decoration: "flat") {
            state "default", label: "Ready", icon:"https://smartthings-device-icons.s3.amazonaws.com/ring/ring_ready_tile.png", backgroundColor:"#CCCCCC"
            state "pushed", label: "Ding-Dong!", icon:"https://smartthings-device-icons.s3.amazonaws.com/ring/ring_ding_tile.png", backgroundColor:"#00A0DC"
        }
        standardTile("motion", "device.motion", width: 2, height: 2, decoration: "flat") {
            state "inactive", label:'No-Motion', icon:"st.motion.motion.inactive", backgroundColor:"#CCCCCC"
            state "active", label:'Motion', icon:"st.motion.motion.active", backgroundColor:"#00A0DC"
        }
        main(["videoPlayer"])
        details(["videoPlayer", "button", "motion"])
    }
}

mappings { path("/getInHomeURL") { action: [ GET: "getInHomeURL" ] } }
def getInHomeURL() { [InHomeURL: ""] }

void startStream() {
    log.trace "[DTH] Executing startStream() for device=${this.device.displayName}"
    parent.startLiveStream(this)
}

void stopStream() {
    log.trace "[DTH] Executing stopStream() for device=${this.device.displayName}"
    parent.stopLiveStream(this)
}

def sdpOffer(id, sdp) {
    log.trace "[DTH] Executing 'sdpOffer()' for device=${this.device.displayName}"
    parent.startWebRTCStream(this, id, sdp)
}

def end(id) {
    log.trace "[DTH] Executing 'end()' for device=${this.device.displayName}"
    parent.stopWebRTCStream(this, id)
}

def handleMotionEvent(eventDescription) {
    log.trace "[DTH] Exectuting handleMotionEvent() for device=${this.device.displayName}"
    sendEvent( name : "motion", value : "active", descriptionText: "${eventDescription}")
    runIn(40, setMotionInactive, [overwrite: true])  //we do not get motion stop events so we use a timer to deactivate tile
}

// Set motion inactive, unless we have a subequent ding event
def setMotionInactive() {
    log.trace "[DTH] Exectuting setMotionInactive() for device=${this.device.displayName}"
    def currentButton = device.currentValue('button')
    log.info "[DTH] setMotionInactive() - The button state is: $currentButton"

    if ( currentButton != "pushed" ) { //don't clear motion if we currently have an active ding
        log.info "[DTH] setMotionInactive() - No current ding event. Clearing motion by timer"
        sendEvent( name : "motion", value : "inactive", displayed: true)
        sendEvent( name : "button", value : "default", data: [buttonNumber: 1], displayed: false)
    } else {
        log.info "[DTH] setMotionInactive() - Currently in a ding event. Not clearing motion"
    }
}

def handleButtonEvent(eventDescription) {
    log.trace "[DTH] Exectuting handleButtonEvent() for device=${this.device.displayName}"
    sendEvent( name : "button", value : "pushed", data: [buttonNumber: 1], descriptionText: "${eventDescription}")
    //we do not get button up events so we use a timer to deactivate tile
    runIn(30, setDingInactive, [overwrite: true])
}

def setDingInactive() {
    log.trace "[DTH] Exectuting setDingInactive() for device=${this.device.displayName}"
    sendEvent( name : "button", value : "default", data: [buttonNumber: 1], displayed: false)
    sendEvent( name : "motion", value : "inactive", displayed: true)
}

void available() {
    log.trace "[DTH] Executing 'available()' for device=${this.device.displayName}"
    sendEvent(name: "DeviceWatch-DeviceStatus", value: "online")
    sendEvent(name: "active", value: "Active")
}

void unavailable() {
    log.trace "[DTH] Executing 'unavailable()' for device=${this.device.displayName}"
    sendEvent(name: "DeviceWatch-DeviceStatus", value: "offline")
    log.warn "'${device.displayName}' is not connected. Check that it is operating correctly."
}

def uninstalled() {
    log.trace "[DTH] Executing 'uninstalled()' for device=${this.device.displayName}"
    parent.removeChildDevice(this)
}

def installed() {
    log.trace "[DTH] Executing 'installed()' for device=${this.device.displayName}"
    initialize()
}

def updated() {
//nothing to update as there are no settings
}

def initialize() {
    log.trace "[DTH] Executing 'intialize()' for device=${this.device.displayName}"
    sendEvent(name: "DeviceWatch-Enroll", value: toJson([protocol: "cloud", scheme:"untracked"]), displayed: false)
    sendEvent(name: "numberOfButtons", value: 1, displayed: false)
    sendEvent(name: "camera", value: "on", displayed: false)
    sendEvent(name: "motion", value: "inactive", displayed: false)
    sendEvent(name: "statusMessage", value: "Active", displayed: false)
    sendEvent(name: "active", value: "Active", displayed: false)
    sendEvent(name: "errorMessage", value: "", displayed: false)
}

def refresh() {}