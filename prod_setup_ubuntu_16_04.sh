#!/bin/bash

sudo apt update
sudo apt install -y software-properties-common
sudo apt-add-repository --yes --update ppa:ansible/ansible
sudo apt -y install ansible

curl -sL https://deb.nodesource.com/setup_14.x | sudo -E bash -

sudo apt-get -y install openjdk-8-jdk
sudo apt-get -y install maven 

mvn clean package install -P external,frontend -DskipTests=true

ansible-playbook ./devops/vtn20b_postgres/provisioning/playbook.yml
