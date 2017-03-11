#! /bin/sh

ulimit -c 100000000

cp ../target/chitanka.azw2 .

echo ================================================================
echo Install updates:
echo ================================================================

./kindletool create ota -d dxg install.sh chitanka.azw2 developer.keystore external.policy update_chitanka_dxg_install.bin
./kindletool create ota -d k3g install.sh chitanka.azw2 developer.keystore external.policy update_chitanka_k3g_install.bin
./kindletool create ota -d k3gb install.sh chitanka.azw2 developer.keystore external.policy update_chitanka_k3gb_install.bin
./kindletool create ota -d k3w install.sh chitanka.azw2 developer.keystore external.policy update_v_k3w_install.bin
./kindletool create ota2 -d k4 -d k4b install.sh chitanka.azw2 developer.keystore external.policy update_chitanka_k4x_install.bin


echo ================================================================
echo Uninstall updates:
echo ================================================================

./kindletool create ota -d dxg uninstall.sh update_chitanka_dxg_uninstall.bin
./kindletool create ota -d k3g uninstall.sh update_chitanka_k3g_uninstall.bin
./kindletool create ota -d k3gb uninstall.sh update_chitanka_k3gb_uninstall.bin
./kindletool create ota -d k3w uninstall.sh update_chitanka_k3w_uninstall.bin
./kindletool create ota2 -d k4 -d k4b uninstall.sh update_chitanka_k4x_uninstall.bin
