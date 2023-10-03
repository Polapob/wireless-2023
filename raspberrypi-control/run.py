import serial
import time
import requests
import logging
# create logger
logger = logging.getLogger("smart_farming")
logger.setLevel(logging.DEBUG)
# create console handler and set level to debug
ch = logging.StreamHandler()
ch.setLevel(logging.DEBUG)
# create formatter
formatter = logging.Formatter("%(asctime)s;%(levelname)s: %(message)s")
# add formatter to ch
ch.setFormatter(formatter)

# add ch to logger
logger.addHandler(ch)

period_secs = 1
ser = serial.Serial("/dev/ttyUSB0", 9600)
bs = ser.readline()

while True:
    ser.write(b'a')
    bs = ser.readline()
    bs = float(bs.decode("utf-8"))
    logger.info(f"Sensor Moisture Value: {bs}")
    try:
        res = requests.post('http://172.20.10.3:8080/waterpump/openPump', timeout=2, json={
        "moisture": bs
        })
        response_json = res.json()
    except Exception as e:
        logger.error(f"API Error with {e}")
        pass
    is_open = response_json.get("isOpen", False)
    logger.info(f"Pump turn on: {is_open}")
    if is_open:
        ser.write(b'a') # write a turn on
    else:
        ser.write(b'b')
    logger.info(f"Sleeping for: {period_secs} seconds")
    time.sleep(period_secs)