echo -e "\n ========Registration Carol========"
echo Response $(registration "Carol" "Carolov" "$random_string@c.ru" "carol")
sleep 1;

echo -e "\n ========Authorisation Carol========"
response=$(authorization "$random_string@c.ru" "carol")
echo Response $response

AUTH_TOKEN_CAROL=$(echo "${response::-4}" | jq -r '.token')
sleep 1;

echo -e "\n ========Get Secret By Token For Carol========"
response=$(create_secret_by_token $AUTH_TOKEN_CAROL $share_token)
echo Response "$(echo "$response" | tail -c 4)"
echo Response code: "${response::-4}"
sleep 1;