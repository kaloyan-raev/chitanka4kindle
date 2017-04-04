#!/bin/sh
sudo ifconfig enp0s20u2 192.168.15.201
scp root@192.168.15.244:/mnt/us/developer/Читанка/work/crash.log .
