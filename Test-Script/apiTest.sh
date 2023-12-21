#!/bin/bash

API_URL="http://localhost:8080/api/v1"

authorization() {
  local url="$API_URL/auth/signin"
  local DATA='{
    "email": "'"$1"'",
    "password": "'"$2"'"
  }'

  response=$(curl -X POST -H "Content-Type: application/json" -d "$DATA" "$url")

  echo "$response"
}

registration() {
  local url="$API_URL/auth/signup"
  local DATA='{
    "firstName": "'"$1"'",
    "lastName": "'"$2"'",
    "email": "'"$3"'",
    "password": "'"$4"'"
  }'

  response=$(curl -X POST -H "Content-Type: application/json" -d "$DATA" "$url")

  echo "$response"
}

create_secret() {
  local url="$API_URL/resource/secret/create"

  local DATA='{
    "name": "'"$2"'",
    "url": "'"$3"'",
    "login": "'"$4"'",
    "password": "'"$5"'"
  }'

  response=$(curl -X POST -H "Authorization: Bearer $1" -H "Content-Type: application/json" -d "$DATA" "$url")

  echo "$response"
}

get_all() {
  local url="$API_URL/resource/secret/get_all"

  response=$(curl -X GET -H "Authorization: Bearer $1" "$url")

  echo "$response"
}

get_one() {
  local url="$API_URL/resource/secret/get?id=$2"

  response=$(curl -X GET -H "Authorization: Bearer $1" "$url")

  echo "$response"
}

get_share_token() {
  local url="$API_URL/resource/token/share?id=$2&days=1"

  response=$(curl -X GET -H "Authorization: Bearer $1" "$url")

  echo "$response"
}

create_secret_by_token() {
  local url="$API_URL/resource/token/add_secret_by_token?token=$2"

  response=$(curl  -w "%{http_code}" -X POST -H "Authorization: Bearer $1" "$url")

  echo "$response"
}

echo ""
echo ========Registration Alisa========
random_string=$(head /dev/urandom | tr -dc 'a-zA-Z0-9' | head -c 4)
echo Response $(registration "Alisa" "Alisovna" "$random_string@a.ru" "alisa")
sleep 1;

echo ""
echo ========Authorisation Alisa========
auth_response=$(authorization "$random_string@a.ru" "alisa")
echo Response $auth_response

AUTH_TOKEN_ALISA=$(echo "$auth_response" | jq -r '.token')
sleep 1;

echo ""
echo ========Create Secrets========
response=$(create_secret $AUTH_TOKEN_ALISA secret1 secret1 secret1 secret1)
echo Response $response
sleep 1;

response=$(create_secret $AUTH_TOKEN_ALISA secret2 secret2 secret2 secret2)
echo Response $response
sleep 1;

response=$(create_secret $AUTH_TOKEN_ALISA secret3 secret3 secret3 secret3)
echo Response $response
sleep 1;

echo ""
echo ========Get all Secrets========
response=$(get_all $AUTH_TOKEN_ALISA)
echo Response $response

secret_id=$(echo "$response" | jq -r '.secrets[0].id')
sleep 1;

echo ""
echo ========Get One Secret========
response=$(get_one $AUTH_TOKEN_ALISA $secret_id)
echo Response $response
sleep 1;

echo ""
echo ========Create Share Secret Token========
response=$(get_share_token $AUTH_TOKEN_ALISA $secret_id)
echo Response $response

share_token=$(echo "$response" | jq -r '.id')
sleep 1;

echo ""
echo ========Registration Bob========
echo Response $(registration "Bob" "Bobov" "$random_string@b.ru" "bob")
sleep 1;

echo ""
echo ========Authorisation Bob========
auth_response=$(authorization "$random_string@b.ru" "bob")
echo Response $auth_response

AUTH_TOKEN_BOB=$(echo "$auth_response" | jq -r '.token')
sleep 1;

echo ""
echo ========Get Secret By Token For Bob========
response=$(create_secret_by_token $AUTH_TOKEN_BOB $share_token)
echo Response $response
http_code=$(echo "$response" | tail -c 4)
echo Response code: $http_code
sleep 1;


echo ""
echo ========Registration Carol========
echo Response $(registration "Carol" "Carolov" "$random_string@c.ru" "carol")
sleep 1;

echo ""
echo ========Authorisation Carol========
auth_response=$(authorization "$random_string@c.ru" "carol")
echo Response $auth_response

AUTH_TOKEN_CAROL=$(echo "$auth_response" | jq -r '.token')
sleep 1;

echo ""
echo ========Get Secret By Token For Carol========
response=$(create_secret_by_token $AUTH_TOKEN_CAROL $share_token)
echo Response $response
http_code=$(echo "$response" | tail -c 4)
echo Response code: $http_code
sleep 1;