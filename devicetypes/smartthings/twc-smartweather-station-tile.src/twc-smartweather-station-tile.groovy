/**
 *  Copyright 2015 SmartThings
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
 *  SmartWeather Station
 *
 *  Author: SmartThings
 *
 *  Date: 2013-04-30
 */
metadata {
    definition (name: "TWC SmartWeather Station Tile", namespace: "smartthings", author: "SmartThings", 
    		ocfDeviceType: "oic.d.thermostat", 
            mnmn: "SmartThings", 
            vid:"SmartThings-smartthings-SmartSense_Temp/Humidity_Sensor") {
            
        capability "Illuminance Measurement"
        capability "Temperature Measurement"
        capability "Relative Humidity Measurement"
        capability "Ultraviolet Index"
        capability "Sensor"
        capability "Refresh"

        attribute "localSunrise", "string"
        attribute "localSunset", "string"
        attribute "city", "string"
        attribute "timeZoneOffset", "string"
        attribute "weather", "string"
        attribute "wind", "string"
        attribute "windVector", "string"
        attribute "weatherIcon", "string"
        attribute "forecastIcon", "string"
        attribute "feelsLike", "string"
        attribute "percentPrecip", "string"
        attribute "alert", "string"
        attribute "alertKeys", "string"
        attribute "sunriseDate", "string"
        attribute "sunsetDate", "string"
        attribute "lastUpdate", "string"
        attribute "uvDescription", "string"
        attribute "forecastToday", "string"
        attribute "forecastTonight", "string"
        attribute "forecastTomorrow", "string"
    }

    preferences {
        input "zipCode", "text", title: "Zip Code (optional)", required: false
    }

    tiles(scale: 2) {
        valueTile("temperature", "device.temperature", height: 2, width: 2) {
            state "default", label:'${currentValue}??',
                    backgroundColors:[
                            [value: 31, color: "#153591"],
                            [value: 44, color: "#1e9cbb"],
                            [value: 59, color: "#90d2a7"],
                            [value: 74, color: "#44b621"],
                            [value: 84, color: "#f1d801"],
                            [value: 95, color: "#d04e00"],
                            [value: 96, color: "#bc2323"]
                    ]
        }

        valueTile("feelsLike", "device.feelsLike", decoration: "flat", height: 1, width: 2) {
            state "default", label:'Feels like ${currentValue}??'
        }

        standardTile("weatherIcon", "device.weatherIcon", decoration: "flat", height: 2, width: 2) {
            state "00", icon:"https://smartthings-twc-icons.s3.amazonaws.com/00.png", label: ""
            state "01", icon:"https://smartthings-twc-icons.s3.amazonaws.com/01.png", label: ""
            state "02", icon:"https://smartthings-twc-icons.s3.amazonaws.com/02.png", label: ""
            state "03", icon:"https://smartthings-twc-icons.s3.amazonaws.com/03.png", label: ""
            state "04", icon:"https://smartthings-twc-icons.s3.amazonaws.com/04.png", label: ""
            state "05", icon:"https://smartthings-twc-icons.s3.amazonaws.com/05.png", label: ""
            state "06", icon:"https://smartthings-twc-icons.s3.amazonaws.com/06.png", label: ""
            state "07", icon:"https://smartthings-twc-icons.s3.amazonaws.com/07.png", label: ""
            state "08", icon:"https://smartthings-twc-icons.s3.amazonaws.com/08.png", label: ""
            state "09", icon:"https://smartthings-twc-icons.s3.amazonaws.com/09.png", label: ""
            state "10", icon:"https://smartthings-twc-icons.s3.amazonaws.com/10.png", label: ""
            state "11", icon:"https://smartthings-twc-icons.s3.amazonaws.com/11.png", label: ""
            state "12", icon:"https://smartthings-twc-icons.s3.amazonaws.com/12.png", label: ""
            state "13", icon:"https://smartthings-twc-icons.s3.amazonaws.com/13.png", label: ""
            state "14", icon:"https://smartthings-twc-icons.s3.amazonaws.com/14.png", label: ""
            state "15", icon:"https://smartthings-twc-icons.s3.amazonaws.com/15.png", label: ""
            state "16", icon:"https://smartthings-twc-icons.s3.amazonaws.com/16.png", label: ""
            state "17", icon:"https://smartthings-twc-icons.s3.amazonaws.com/17.png", label: ""
            state "18", icon:"https://smartthings-twc-icons.s3.amazonaws.com/18.png", label: ""
            state "19", icon:"https://smartthings-twc-icons.s3.amazonaws.com/19.png", label: ""
            state "20", icon:"https://smartthings-twc-icons.s3.amazonaws.com/20.png", label: ""
            state "21", icon:"https://smartthings-twc-icons.s3.amazonaws.com/21.png", label: ""
            state "22", icon:"https://smartthings-twc-icons.s3.amazonaws.com/22.png", label: ""
            state "23", icon:"https://smartthings-twc-icons.s3.amazonaws.com/23.png", label: ""
            state "24", icon:"https://smartthings-twc-icons.s3.amazonaws.com/24.png", label: ""
            state "25", icon:"https://smartthings-twc-icons.s3.amazonaws.com/25.png", label: ""
            state "26", icon:"https://smartthings-twc-icons.s3.amazonaws.com/26.png", label: ""
            state "27", icon:"https://smartthings-twc-icons.s3.amazonaws.com/27.png", label: ""
            state "28", icon:"https://smartthings-twc-icons.s3.amazonaws.com/28.png", label: ""
            state "29", icon:"https://smartthings-twc-icons.s3.amazonaws.com/29.png", label: ""
            state "30", icon:"https://smartthings-twc-icons.s3.amazonaws.com/30.png", label: ""
            state "31", icon:"https://smartthings-twc-icons.s3.amazonaws.com/31.png", label: ""
            state "32", icon:"https://smartthings-twc-icons.s3.amazonaws.com/32.png", label: ""
            state "33", icon:"https://smartthings-twc-icons.s3.amazonaws.com/33.png", label: ""
            state "34", icon:"https://smartthings-twc-icons.s3.amazonaws.com/34.png", label: ""
            state "35", icon:"https://smartthings-twc-icons.s3.amazonaws.com/35.png", label: ""
            state "36", icon:"https://smartthings-twc-icons.s3.amazonaws.com/36.png", label: ""
            state "37", icon:"https://smartthings-twc-icons.s3.amazonaws.com/37.png", label: ""
            state "38", icon:"https://smartthings-twc-icons.s3.amazonaws.com/38.png", label: ""
            state "39", icon:"https://smartthings-twc-icons.s3.amazonaws.com/39.png", label: ""
            state "40", icon:"https://smartthings-twc-icons.s3.amazonaws.com/40.png", label: ""
            state "41", icon:"https://smartthings-twc-icons.s3.amazonaws.com/41.png", label: ""
            state "42", icon:"https://smartthings-twc-icons.s3.amazonaws.com/42.png", label: ""
            state "43", icon:"https://smartthings-twc-icons.s3.amazonaws.com/43.png", label: ""
            state "44", icon:"https://smartthings-twc-icons.s3.amazonaws.com/44.png", label: ""
            state "45", icon:"https://smartthings-twc-icons.s3.amazonaws.com/45.png", label: ""
            state "46", icon:"https://smartthings-twc-icons.s3.amazonaws.com/46.png", label: ""
            state "47", icon:"https://smartthings-twc-icons.s3.amazonaws.com/47.png", label: ""
            state "na", icon:"https://smartthings-twc-icons.s3.amazonaws.com/na.png", label: ""
        }

        valueTile("humidity", "device.humidity", decoration: "flat", height: 1, width: 2) {
            state "default", label:'${currentValue}% humidity'
        }

        valueTile("wind", "device.windVector", decoration: "flat", height: 1, width: 2) {
            state "default", label:'Wind\n${currentValue}'
        }

        valueTile("weather", "device.weather", decoration: "flat", height: 1, width: 2) {
            state "default", label:'${currentValue}'
        }

        valueTile("city", "device.city", decoration: "flat", height: 1, width: 2) {
            state "default", label:'${currentValue}'
        }

        valueTile("percentPrecip", "device.percentPrecip", decoration: "flat", height: 1, width: 2) {
            state "default", label:'${currentValue}% precip'
        }

        valueTile("ultravioletIndex", "device.uvDescription", decoration: "flat", height: 1, width: 2) {
            state "default", label:'UV ${currentValue}'
        }

        valueTile("alert", "device.alert", decoration: "flat", height: 2, width: 6) {
            state "default", label:'${currentValue}'
        }

        standardTile("refresh", "device.weather", decoration: "flat", height: 1, width: 2) {
            state "default", label: "", action: "refresh", icon:"st.secondary.refresh"
        }

        valueTile("rise", "device.localSunrise", decoration: "flat", height: 1, width: 2) {
            state "default", label:'Sunrise ${currentValue}'
        }

        valueTile("set", "device.localSunset", decoration: "flat", height: 1, width: 2) {
            state "default", label:'Sunset ${currentValue}'
        }

        valueTile("light", "device.illuminance", decoration: "flat", height: 1, width: 2) {
            state "default", label:'${currentValue} lux'
        }

        valueTile("today", "device.forecastToday", decoration: "flat", height: 1, width: 3) {
            state "default", label:'Today:\n${currentValue}'
        }

        valueTile("tonight", "device.forecastTonight", decoration: "flat", height: 1, width: 3) {
            state "default", label:'Tonight:\n${currentValue}'
        }

        valueTile("tomorrow", "device.forecastTomorrow", decoration: "flat", height: 1, width: 3) {
            state "default", label:'Tomorrow:\n${currentValue}'
        }

        valueTile("lastUpdate", "device.lastUpdate", decoration: "flat", height: 1, width: 3) {
            state "default", label:'Last update:\n${currentValue}'
        }

        main(["temperature", "weatherIcon","feelsLike"])
        details(["temperature", "feelsLike", "weatherIcon", "humidity", "wind",
                 "weather", "city", "percentPrecip", "ultravioletIndex", "light",
                 "rise", "set",
                 "refresh",
                 "today", "tonight", "tomorrow", "lastUpdate",
                 "alert"])}
}

// parse events into attributes
def parse(String description) {
    log.debug "Parsing '${description}'"
}

def installed() {
    poll()
    runEvery30Minutes(poll)
}

def uninstalled() {
    unschedule()
}

// handle commands
def poll() {
    log.info "WUSTATION: Executing 'poll', location: ${location.name}"

    // Last update time stamp
    def timeStamp = new Date().format("yyyy MMM dd EEE h:mm:ss a", location.timeZone)
    sendEvent(name: "lastUpdate", value: timeStamp)

    // Current conditions
    def tempUnits = getTemperatureScale()
    def windUnits = tempUnits == "C" ? "KPH" : "MPH"
    def obs = getTwcConditions(zipCode)
    if (obs) {
        // TODO def weatherIcon = obs.icon_url.split("/")[-1].split("\\.")[0]

        send(name: "temperature", value: obs.temperature, unit: tempUnits)
        send(name: "feelsLike", value: obs.temperatureFeelsLike, unit: tempUnits)

        send(name: "humidity", value: obs.relativeHumidity, unit: "%")
        send(name: "weather", value: obs.wxPhraseShort)
        send(name: "weatherIcon", value: obs.iconCode as String, displayed: false)
        send(name: "wind", value: obs.windSpeed as String, unit: windUnits) // as String because of bug in determining state change of 0 numbers
        send(name: "windVector", value: "${obs.windDirectionCardinal} ${obs.windSpeed} ${windUnits}")
        log.trace "Getting location info"
        def loc = getTwcLocation(zipCode).location
        def cityValue = "${loc.city}, ${loc.adminDistrictCode} ${loc.countryCode}"
        if (cityValue != device.currentValue("city")) {
            send(name: "city", value: cityValue, isStateChange: true)
        }

        send(name: "ultravioletIndex", value: obs.uvIndex)
        send(name: "uvDescription", value: obs.uvDescription)

        def dtf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")

        def sunriseDate = dtf.parse(obs.sunriseTimeLocal)
        log.info "'${obs.sunriseTimeLocal}'"

        def sunsetDate = dtf.parse(obs.sunsetTimeLocal)

        def tf = new java.text.SimpleDateFormat("h:mm a")
        tf.setTimeZone(TimeZone.getTimeZone(loc.ianaTimeZone))

        def localSunrise = "${tf.format(sunriseDate)}"
        def localSunset = "${tf.format(sunsetDate)}"
        send(name: "localSunrise", value: localSunrise, descriptionText: "Sunrise today is at $localSunrise")
        send(name: "localSunset", value: localSunset, descriptionText: "Sunset today at is $localSunset")

        send(name: "illuminance", value: estimateLux(obs, sunriseDate, sunsetDate))

        // Forecast
        def f = getTwcForecast(zipCode)
        if (f) {
            def icon = f.daypart[0].iconCode[0] ?: f.daypart[0].iconCode[1]
            def value = f.daypart[0].precipChance[0] ?: f.daypart[0].precipChance[1]
            def narrative = f.daypart[0].narrative
            send(name: "percentPrecip", value: value, unit: "%")
            send(name: "forecastIcon", value: icon, displayed: false)
            send(name: "forecastToday", value: narrative[0])
            send(name: "forecastTonight", value: narrative[1])
            send(name: "forecastTomorrow", value: narrative[2])
        }
        else {
            log.warn "Forecast not found"
        }

        // Alerts
        def alerts = getTwcAlerts(zipCode)
        if (alerts) {
            alerts.each {alert ->
                def msg = alert.headlineText
                if (alert.effectiveTimeLocal && !msg.contains(" from ")) {
                    msg += " from ${parseAlertTime(alert.effectiveTimeLocal).format("E hh:mm a", TimeZone.getTimeZone(alert.effectiveTimeLocalTimeZone))}"
                }
                if (alert.expireTimeLocal && !msg.contains(" until ")) {
                    msg += " until ${parseAlertTime(alert.expireTimeLocal).format("E hh:mm a", TimeZone.getTimeZone(alert.expireTimeLocalTimeZone))}"
                }
                send(name: "alert", value: msg, descriptionText: msg)
            }
        }
        else {
            send(name: "alert", value: "No current alerts", descriptionText: msg)
        }
    }
    else {
        log.warn "No response from TWC API"
    }
}

def parseAlertTime(s) {
    def dtf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
    def s2 = s.replaceAll(/([0-9][0-9]):([0-9][0-9])$/,'$1$2')
    dtf.parse(s2)
}

def refresh() {
    poll()
}

def configure() {
    poll()
}

private pad(String s, size = 25) {
    def n = (size - s.size()) / 2
    if (n > 0) {
        def sb = ""
        n.times {sb += " "}
        sb += s
        n.times {sb += " "}
        return sb
    }
    else {
        return s
    }
}


private get(feature) {
    getWeatherFeature(feature, zipCode)
}

private localDate(timeZone) {
    def df = new java.text.SimpleDateFormat("yyyy-MM-dd")
    df.setTimeZone(TimeZone.getTimeZone(timeZone))
    df.format(new Date())
}

private send(map) {
    log.debug "WUSTATION: event: $map"
    sendEvent(map)
}

private estimateLux(obs, sunriseDate, sunsetDate) {
    def lux = 0
    if (obs.dayOrNight == 'N') {
        lux = 10
    }
    else {
        //day
        switch(obs.iconCode) {
            case '04':
                lux = 200
                break
            case ['05', '06', '07', '08', '09', '10',
                  '11', '12', '13','14', '15','17','18','19','20',
                  '21','22','23','24','25','26']:
                lux = 1000
                break
            case ['27', '28']:
                lux = 2500
                break
            case ['29', '30']:
                lux = 7500
                break
            default:
                //sunny, clear
                lux = 10000
        }

        //adjust for dusk/dawn
        def now = new Date().time
        def afterSunrise = now - sunriseDate.time
        def beforeSunset = sunsetDate.time - now
        def oneHour = 1000 * 60 * 60

        if(afterSunrise < oneHour) {
            //dawn
            lux = (long)(lux * (afterSunrise/oneHour))
        } else if (beforeSunset < oneHour) {
            //dusk
            lux = (long)(lux * (beforeSunset/oneHour))
        }
    }
    lux
}