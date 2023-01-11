####
# Before building the container image run:
#
# ng build
#
# Then, build the image with:
#
# docker build -t angular/newsletter:1.0 .
#
# Then run the container using:
#
# docker run -i --rm -p 80:80 --name angular/newsletter -e PORT=80 -e API_URL=http://localhost:8080 angular/newsletter:1.0
#
###

FROM nginx:latest

COPY dist/web-angular /usr/share/nginx/html

COPY ./nginx/default.conf.template /etc/nginx/conf.d/default.conf.template

CMD ["/bin/bash", "-c", \
"echo API_URL=[$API_URL], && \
echo PORT=[$PORT], && \
sed -i s#API_URL_WILL_BE_REPLACED#$API_URL#g /usr/share/nginx/html/main.*.js && \
envsubst '$PORT' < /etc/nginx/conf.d/default.conf.template > /etc/nginx/conf.d/default.conf && \
nginx -g 'daemon off;'"]
