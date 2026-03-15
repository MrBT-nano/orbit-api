FROM nginx:alpine
COPY ./docs/out /usr/share/nginx/html/orbit-api
COPY ./docs/public/favicon.webp /usr/share/nginx/html/favicon.webp
COPY ./docs/public/favicon.webp /usr/share/nginx/html/favicon.ico
RUN cat > /etc/nginx/conf.d/default.conf << 'EOF'
server {
    listen 3000;
    root /usr/share/nginx/html;

    location / {
        try_files $uri $uri.html $uri/index.html =404;
    }
}
EOF
EXPOSE 3000
