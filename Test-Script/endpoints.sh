#!/bin/bash
declare LAST_HTTP_CODE=0

authorization() {
  local url="$API_URL/auth/signin"
  local data='{
    "email": "'"$1"'",
    "password": "'"$2"'"
  }'

  response=$(curl -w "%{http_code}" -X POST -H "Content-Type: application/json" -d "$data" "$url")

  echo "${response::-3} $(echo "$response" | tail -c 4)"
}
export -f authorization

registration() {
  local url="$API_URL/auth/signup"
  local data='{
    "firstName": "'"$1"'",
    "lastName": "'"$2"'",
    "email": "'"$3"'",
    "password": "'"$4"'"
  }'

  response=$(curl -w "%{http_code}" -X POST -H "Content-Type: application/json" -d "$data" "$url")

  echo "${response::-3} $(echo "$response" | tail -c 4)"
}
export -f registration


create_secret() {
  local url="$API_URL/resource/secret/create"

  local data='{
    "name": "'"$2"'",
    "url": "'"$3"'",
    "login": "'"$4"'",
    "password": "'"$5"'",
    "directoryId": "'"$6"'"
  }'

  response=$(curl -w "%{http_code}" -X POST -H "Authorization: Bearer $1" -H "Content-Type: application/json" -d "$data" "$url")
  
  echo "${response::-3} $(echo "$response" | tail -c 4)"
}
export -f create_secret

get_all() {
  local url="$API_URL/resource/secret/get_all"

  response=$(curl -w "%{http_code}" -X GET -H "Authorization: Bearer $1" "$url")

  echo "${response::-3} $(echo "$response" | tail -c 4)"
}
export -f get_all

get_one() {
  local url="$API_URL/resource/secret/get?id=$2"

  response=$(curl -w "%{http_code}" -X GET -H "Authorization: Bearer $1" "$url")

  echo "${response::-3} $(echo "$response" | tail -c 4)"
}
export -f get_one

get_share_token() {
  days=1;

  if [[ $# -gt 2 ]]; then
    days=$3
  fi

  local url="$API_URL/resource/token/share?id=$2&days=$days"

  response=$(curl -w "%{http_code}" -X GET -H "Authorization: Bearer $1" "$url")

  echo "${response::-3} $(echo "$response" | tail -c 4)"
}
export -f get_share_token

create_secret_by_token() {
  local url="$API_URL/resource/token/add_secret_by_token?token=$2"

  response=$(curl -w "%{http_code}" -X POST -H "Authorization: Bearer $1" "$url")

  echo "${response::-3} $(echo "$response" | tail -c 4)"
}
export -f create_secret_by_token

create_directory() {
  local url="$API_URL/resource/directory/create"

  local data='{
    "name": "'"$2"'",
    "parentId": "'"$3"'"
  }'

  response=$(curl -w "%{http_code}" -X POST -H "Authorization: Bearer $1" -H "Content-Type: application/json" -d "$data" "$url")

  echo "${response::-3} $(echo "$response" | tail -c 4)"
}
