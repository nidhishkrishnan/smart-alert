/**
 * This file/module contains all configuration for the build process.
 */
module.exports = {
    /**
     * The `build_dir` folder is where our projects are compiled during
     * development and the `compile_dir` folder is where our app resides once it's
     * completely built.
     */
    build_dir: 'D:/NidhishKrishnan/Softwares/apache-tomcat-7.0.34/webapps/Smart',
    compile_dir: 'bin',
    webapp_dir: '../src/main/webapp',

    /**
     * This is a collection of file patterns that refer to our app code (the
     * stuff in `src/`). These file paths are used in the configuration of
     * build tasks. `js` is all project javascript, less tests. `ctpl` contains
     * our reusable components' (`src/common`) template HTML files, while
     * `atpl` contains the same, but for our app's code. `html` is just our
     * main HTML file, `less` is our main stylesheet, and `unit` contains our
     * app's unit tests.
     */
    app_files: {
        js: ['src/**/*.js', '!src/**/*.spec.js', '!src/assets/**/*.js'],
        jsunit: ['src/**/*.spec.js'],

        coffee: ['src/**/*.coffee', '!src/**/*.spec.coffee'],
        coffeeunit: ['src/**/*.spec.coffee'],

        atpl: ['src/app/**/*.tpl.html', 'src/app/**/*.html', ],
        ctpl: ['src/common/**/*.tpl.html', 'src/common/**/*.html'],

        html: ['src/index.html'],
        less: 'src/less/main.less'
    },

    /**
     * This is a collection of files used during testing only.
     */
    test_files: {
        js: [
            'vendor/angular-mocks/angular-mocks.js'
        ]
    },

    /**
     * This is the same as `app_files`, except it contains patterns that
     * reference vendor code (`vendor/`) that we need to place into the build
     * process somewhere. While the `app_files` property ensures all
     * standardized files are collected for compilation, it is the user's job
     * to ensure non-standardized (i.e. vendor-related) files are handled
     * appropriately in `vendor_files.js`.
     *
     * The `vendor_files.js` property holds files to be automatically
     * concatenated and minified with our project source files.
     *
     * The `vendor_files.css` property holds any CSS files to be automatically
     * included in our app.
     *
     * The `vendor_files.assets` property holds any assets to be copied along
     * with our app's assets. This structure is flattened, so it is not
     * recommended that you use wildcards.
     */
    vendor_files: {
        js: [
            'vendor/jquery/dist/jquery.min.js',
            'vendor/jquery-ui/jquery-ui.min.js',
            'vendor/iCheck/icheck.js',
            'vendor/bootstrap/dist/js/bootstrap.min.js',
            'vendor/metisMenu/dist/metisMenu.min.js',
            'vendor/slimScroll/jquery.slimscroll.min.js',
            // 'vendor/PACE/pace.min.js',
            'vendor/angular/angular.min.js',
            'vendor/angular-sanitize/angular-sanitize.min.js',
            'vendor/angular-ui-router/release/angular-ui-router.min.js',
            'vendor/angular-bootstrap/ui-bootstrap-tpls.min.js',
            'vendor/angular-rt-popup/dist/angular-rt-popup.min.js',
            'vendor/angular-mass-autocomplete/massautocomplete.min.js',
            'vendor/fuse.js/src/fuse.min.js',
            'vendor/angular-auto-validate/dist/jcs-auto-validate.min.js',
            'vendor/ng-lodash/build/ng-lodash.min.js',
            'vendor/oclazyload/dist/ocLazyLoad.js',
            'vendor/moment/moment.js',
            'vendor/angular-bootstrap-datetimepicker/src/js/datetimepicker.js',
            'vendor/lodash/lodash.min.js',
            'vendor/restangular/dist/restangular.min.js',
            'vendor/angular-notify/angular-notify.js',
            'vendor/angular-ui-date/src/date.js',
            'vendor/ng-table/dist/ng-table.js',
            'vendor/ngListSelect/ngListSelect.js',
            'vendor/angular-media-player/dist/angular-media-player.js'
        ],
        css: [
            'vendor/jquery-ui/themes/smoothness/jquery-ui.css',
            'vendor/fontawesome/css/font-awesome.min.css',
            'vendor/angular-bootstrap-datetimepicker/src/css/datetimepicker.css',
            'vendor/angular-notify/angular-notify.css',
            'vendor/ng-table/dist/ng-table.css'
        ],
        assets: [],
        fonts: [
            'vendor/fontawesome/fonts/FontAwesome.otf',
            'vendor/fontawesome/fonts/fontawesome-webfont.eot',
            'vendor/fontawesome/fonts/fontawesome-webfont.svg',
            'vendor/fontawesome/fonts/fontawesome-webfont.ttf',
            'vendor/fontawesome/fonts/fontawesome-webfont.woff',
            'vendor/fontawesome/fonts/fontawesome-webfont.woff2',
            'vendor/bootstrap/fonts/glyphicons-halflings-regular.eot',
            'vendor/bootstrap/fonts/glyphicons-halflings-regular.svg',
            'vendor/bootstrap/fonts/glyphicons-halflings-regular.ttf',
            'vendor/bootstrap/fonts/glyphicons-halflings-regular.woff',
            'vendor/bootstrap/fonts/glyphicons-halflings-regular.woff2'
        ]
    },
};
