/*
* Fill in the values for the variables below in order for this app to know
* more about your local configuration. If a value has a DEFAULT, you only
* need to uncomment it if you wish to customize it.
*/

// The hostname for the Ping Federate instance used with this app.
window.PF_HOST = 'localhost';

// The port for the Ping Federate instance used with this app.
window.PF_PORT = '9031';

// The client id that was set up with Ping Federate and intended to be used by this app.
window.DADMIN_CLIENT_ID = 'dadmin';

// The hostname for the DS instance the app will be interfacing with. By default, the app assumes it
// is hosted alongside your DS instance, in which case it does not need to be specified. Only change
// this variable if you are hosting this application elsewhere.
// DEFAULT: undefined
// window.DS_HOST = undefined;

// The HTTPS port for the DS instance the app will be interfacing with. By default, the app assumes
// it is hosted alongside your DS instance, in which case it does not need to be specified. Only
// change this variable if you are hosting this application elsewhere.
// DEFAULT: undefined
// window.DS_PORT = undefined;

// The length of time (in minutes) until the session will require a new login attempt
// DEFAULT: 30 minutes
// window.TIMEOUT_LENGTH_MINS = 30;

// The filename used as the logo in the header bar, relative to this application's build directory.
// Note about logos: The size of the image will be scaled down to fit 22px of height and a max-width
// of 150px. For best results, it is advised to make the image close to this height and width ratio
// as well as to crop out any blank spacing around the logo to maximize its presentation.
// e.g. 'my_company_logo.png'
// DEFAULT: undefined
// window.HEADER_BAR_LOGO = undefined;

// The namespace for the Delegated Admin API on the DS instance. In most cases, this does not need
// to be set here.
// e.g. 'dadmin/v2'
// DEFAULT: undefined
// window.DADMIN_API_NAMESPACE = undefined;
