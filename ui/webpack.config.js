const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const {CleanWebpackPlugin} = require('clean-webpack-plugin');
const CopyPlugin = require('copy-webpack-plugin');

module.exports = {
    mode: 'development',
    entry: ['react-hot-loader/patch', './src/index.tsx'],
    devtool: 'source-map',
    target: 'web',
    module: {
        rules: [
            {
                test: /\.(tsx?)|(js)$/,
                exclude: /node_modules/,
                loader: 'babel-loader'
            },
            {
                test: /\.css$/,
                use: [
                    'style-loader',
                    'css-loader'
                ]
            },
            {
                test: /\.less$/,
                use: [
                    'style-loader',
                    {
                        loader: 'css-loader',
                        options: {
                            modules: {
                                auto: /^.+$/i,
                                localIdentName: '[name]_[local]___[hash:base64:5]',
                            },
                            importLoaders: 1
                        }
                    },
                    {
                        loader: 'less-loader'
                    }
                ]
            },
            {
                test: /\.(png|gif|eot|svg|ttf|woff(2)?|(jpeg|jpg)(\?[0-9]+)?)$/,
                use: [{loader: 'base64-inline-loader?limit32000&name=[name].[ext]'}]
            }
        ]
    },
    resolve: {
        extensions: ['.tsx', '.ts', '.js', '.jsx'],
        alias: {
            'app': path.resolve(__dirname, 'src/app'),
            'react': path.resolve(__dirname, 'node_modules/react'),
            'react-dom': '@hot-loader/react-dom',
            'react-redux': path.resolve(__dirname, 'node_modules/react-redux'),
            'react-router': path.resolve(__dirname, 'node_modules/react-router'),
            'react-router-dom': path.resolve(__dirname, 'node_modules/react-router-dom'),
            'react-router-hash-link': path.resolve(__dirname, 'node_modules/react-router-hash-link'),
        }
    },

    plugins: [
        new CleanWebpackPlugin(),
        new CopyPlugin({ patterns: [{from: './static/favicon.ico', to:path.resolve(__dirname, './build/public/favicon.ico')}]}),
        new HtmlWebpackPlugin({
            template: path.resolve(__dirname, './static/index.html'),
            filename: 'index.html'
        }),
    ],
    output: {
        filename: 'assets/bundle/js/[name].[contenthash].bundle.js',
        chunkFilename: 'assets/bundle/js/[name].[contenthash].bundle.js',
        path: path.resolve(__dirname, './build/public'),
        publicPath: '',
        clean: true,
    },
    devServer: {
        compress: true,
        port: 9090,
        host: '0.0.0.0',
        historyApiFallback: true,
        disableHostCheck: true,
        hot: true,
        writeToDisk: false,
        inline: true,
        progress: false,
        proxy: [{
                    path: '/',
                    target: 'http://localhost:8080'
                }],
    }
};
