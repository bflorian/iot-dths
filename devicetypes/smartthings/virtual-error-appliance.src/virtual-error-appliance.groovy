/**
 *  Virtual Error Appliance
 *
 *  Copyright 2020 SmartThings
 */
metadata {
	definition (name: "Virtual Error Appliance", namespace: "smartthings", author: "SmartThings", cstHandler: true) {
		capability "Execute"
		capability "Momentary"
        attribute "momentary", "string"
	}


	tiles {
		standardTile("acceleration", "device.momentary", width: 2, height: 2) {
			state("default", label:'Send', action: "push", backgroundColor:"#cccccc", nextState: "sending")
			state("sending", label:'Sending', backgroundColor:"#00a0dc")
		}
	}
    
    preferences {
		input "errorCode", "text", title: "Error Code", defaultValue: "E101"
	}
}

// handle commands
def execute() {
	log.debug "Executing 'execute'"
}

def push() {
	def time = new Date().format("yyyy-MM-dd'T'HH:mm:ss")
	def code = errorCode ?: "E101"
	def data = [
        "payload": [
            "x.com.samsung.da.items": [[
                "x.com.samsung.da.state": "Created",
                "x.com.samsung.da.description": "Alarm",
                "x.com.samsung.da.id": "0",
                "x.com.samsung.da.code": "ErrorCode_${code}",
                "x.com.samsung.da.triggeredTime": time,
                "x.com.samsung.da.alarmType": "Device"
            ]],
            "if": ["oic.if.baseline", "oic.if.a"],
            "rt": ["x.com.samsung.da.alarms"]
        ]
    ]
    log.debug "Sending error code ${code}"
    sendEvent(name: "data", value: data.encodeAsJSON(), isStateChange: true)
    sendEvent(name: "momentary", value: "sent", isStateChange: true)
}